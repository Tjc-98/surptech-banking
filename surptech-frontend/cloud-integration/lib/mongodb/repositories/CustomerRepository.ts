/**
 * Customer Repository
 *
 * MongoDB operations for customer data.
 * Extends BaseRepository with customer-specific queries.
 */

import { Document, ObjectId } from 'mongodb';
import { BaseRepository } from '../repository';

export interface CustomerDocument extends Document {
  _id?: ObjectId;
  socialSecurityNumber: string;
  firstName: string;
  lastName: string;
  address: string;
  fullCreditBalance: number;
  spendBalance: number;
  interestRate: number;
  createdAt: Date;
  updatedAt: Date;
}

export class CustomerRepository extends BaseRepository<CustomerDocument> {
  constructor() {
    super('customers');
  }

  async findBySSN(ssn: string): Promise<CustomerDocument | null> {
    return await this.findOne({ socialSecurityNumber: ssn });
  }

  async findByName(
    firstName?: string,
    lastName?: string
  ): Promise<CustomerDocument[]> {
    const filter: Record<string, unknown> = {};
    if (firstName) filter.firstName = { $regex: firstName, $options: 'i' };
    if (lastName) filter.lastName = { $regex: lastName, $options: 'i' };
    return await this.find(filter);
  }

  async upsertBySSN(
    customer: Omit<CustomerDocument, '_id' | 'createdAt' | 'updatedAt'>
  ): Promise<CustomerDocument> {
    const collection = await this.getCollection();
    const now = new Date();

    const result = await collection.findOneAndUpdate(
      { socialSecurityNumber: customer.socialSecurityNumber },
      {
        $set: { ...customer, updatedAt: now },
        $setOnInsert: { createdAt: now },
      },
      { upsert: true, returnDocument: 'after' }
    );

    return result!;
  }

  async updateCreditInfo(
    ssn: string,
    creditInfo: {
      fullCreditBalance?: number;
      spendBalance?: number;
      interestRate?: number;
    }
  ): Promise<CustomerDocument | null> {
    const collection = await this.getCollection();
    const result = await collection.findOneAndUpdate(
      { socialSecurityNumber: ssn },
      { $set: { ...creditInfo, updatedAt: new Date() } },
      { returnDocument: 'after' }
    );
    return result || null;
  }

  async findHighCreditCustomers(
    minBalance: number,
    limitCount: number = 10
  ): Promise<CustomerDocument[]> {
    return await this.find(
      { fullCreditBalance: { $gte: minBalance } },
      { sort: { fullCreditBalance: -1 }, limit: limitCount }
    );
  }

  async deleteBySSN(ssn: string): Promise<boolean> {
    const collection = await this.getCollection();
    const result = await collection.deleteOne({ socialSecurityNumber: ssn });
    return result.deletedCount > 0;
  }

  async existsBySSN(ssn: string): Promise<boolean> {
    return await this.exists({ socialSecurityNumber: ssn });
  }
}
