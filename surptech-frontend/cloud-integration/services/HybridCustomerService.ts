/**
 * Hybrid Customer Service
 *
 * Cache-first strategy: check MongoDB first, fall back to the backend API,
 * then write the fresh result back to the cache.
 */

import {
  CustomerRepository,
  CustomerDocument,
} from '@/cloud-integration/lib/mongodb/repositories/CustomerRepository';

export interface CustomerData {
  socialSecurityNumber: string;
  firstName: string;
  lastName: string;
  address: string;
  fullCreditBalance: number;
  spendBalance: number;
  interestRate: number;
}

export class HybridCustomerService {
  private repository: CustomerRepository;
  private cacheExpirationMinutes: number;

  constructor(cacheExpirationMinutes = 60) {
    this.repository = new CustomerRepository();
    this.cacheExpirationMinutes = cacheExpirationMinutes;
  }

  async getCustomer(ssn: string): Promise<CustomerData> {
    const cached = await this.getCachedCustomer(ssn);

    if (cached && this.isCacheFresh(cached)) {
      console.log('Cache hit for customer:', ssn);
      return this.toCustomerData(cached);
    }

    console.log('Cache miss for customer:', ssn);
    const fresh = await this.fetchFromBackend(ssn);
    await this.updateCache(fresh);
    return fresh;
  }

  async invalidateCache(ssn: string): Promise<void> {
    try {
      await this.repository.deleteBySSN(ssn);
    } catch (error) {
      console.error('Error invalidating cache:', error);
    }
  }

  async clearAllCache(): Promise<void> {
    try {
      const count = await this.repository.deleteAll();
      console.log(`Cleared ${count} customers from cache`);
    } catch (error) {
      console.error('Error clearing cache:', error);
    }
  }

  async getCacheStats(): Promise<{
    totalCustomers: number;
    freshCustomers: number;
    staleCustomers: number;
  }> {
    try {
      const all = await this.repository.find();
      let freshCount = 0;
      let staleCount = 0;
      all.forEach((c) => (this.isCacheFresh(c) ? freshCount++ : staleCount++));
      return { totalCustomers: all.length, freshCustomers: freshCount, staleCustomers: staleCount };
    } catch {
      return { totalCustomers: 0, freshCustomers: 0, staleCustomers: 0 };
    }
  }

  // ─── Private helpers ────────────────────────────────────────────────────────

  private async getCachedCustomer(ssn: string): Promise<CustomerDocument | null> {
    try {
      return await this.repository.findBySSN(ssn);
    } catch (error) {
      console.error('Error reading from cache:', error);
      return null;
    }
  }

  private isCacheFresh(customer: CustomerDocument): boolean {
    const ageMs = Date.now() - new Date(customer.updatedAt).getTime();
    return ageMs < this.cacheExpirationMinutes * 60 * 1000;
  }

  private async fetchFromBackend(ssn: string): Promise<CustomerData> {
    const response = await fetch(
      `/api/customer/info?socialSecurityNumber=${encodeURIComponent(ssn)}`
    );

    if (!response.ok) {
      throw new Error(`Failed to fetch customer: ${response.statusText}`);
    }

    const data = await response.json();

    return {
      socialSecurityNumber: data.social_security_number,
      firstName: data.first_name,
      lastName: data.last_name,
      address: data.address,
      fullCreditBalance: data.full_credit_balance,
      spendBalance: data.spend_balance,
      interestRate: data.interest_rate,
    };
  }

  private async updateCache(customer: CustomerData): Promise<void> {
    try {
      await this.repository.upsertBySSN(customer);
    } catch (error) {
      // Cache failure must not break the request
      console.error('Error updating cache:', error);
    }
  }

  private toCustomerData(doc: CustomerDocument): CustomerData {
    return {
      socialSecurityNumber: doc.socialSecurityNumber,
      firstName: doc.firstName,
      lastName: doc.lastName,
      address: doc.address,
      fullCreditBalance: doc.fullCreditBalance,
      spendBalance: doc.spendBalance,
      interestRate: doc.interestRate,
    };
  }
}
