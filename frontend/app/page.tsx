'use client';

import { useState } from 'react';
import styles from './page.module.css';

interface CustomerInfo {
  socialSecurityNumber: string;
  firstName: string;
  lastName: string;
  address: string;
  fullCreditBalance: number;
  spendBalance: number;
  interestRate: number;
}

export default function HomePage() {
  const [ssn, setSsn] = useState('');
  const [customer, setCustomer] = useState<CustomerInfo | null>(null);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  async function handleSearch(e: React.FormEvent) {
    e.preventDefault();
    setError('');
    setCustomer(null);

    if (!ssn.trim()) {
      setError('Please enter a Social Security Number.');
      return;
    }

    setLoading(true);
    try {
      const response = await fetch(
        `http://localhost:8080/api/customer/info?socialSecurityNumber=${encodeURIComponent(ssn)}`
      );

      if (response.status === 404) {
        setError(`No customer found for SSN: ${ssn}`);
      } else if (!response.ok) {
        setError('Something went wrong. Please try again.');
      } else {
        const data: CustomerInfo = await response.json();
        setCustomer(data);
      }
    } catch {
      setError('Could not connect to the server. Make sure the backend is running on port 8080.');
    } finally {
      setLoading(false);
    }
  }

  return (
    <main className={styles.main}>
      <h1 className={styles.title}>SurpTech Banking</h1>

      <div className={styles.card}>
        <form onSubmit={handleSearch}>
          <label className={styles.label} htmlFor="ssn">
            Social Security Number
          </label>
          <input
            id="ssn"
            className={styles.input}
            type="text"
            value={ssn}
            onChange={(e) => setSsn(e.target.value)}
            placeholder="e.g. 123-45-6789"
            maxLength={11}
          />
          <button className={styles.button} type="submit" disabled={loading}>
            {loading ? 'Searching...' : 'Look Up'}
          </button>
        </form>

        {error && <p className={styles.error}>{error}</p>}

        <p className={styles.hint}>
          Try: <code>123-45-6789</code> or <code>987-65-4321</code>
        </p>
      </div>

      {customer && (
        <div className={styles.card}>
          <h2 className={styles.sectionTitle}>Customer Profile</h2>
          <table className={styles.table}>
            <tbody>
              <tr>
                <td className={styles.label}>SSN</td>
                <td>{customer.socialSecurityNumber}</td>
              </tr>
              <tr>
                <td className={styles.label}>First Name</td>
                <td>{customer.firstName}</td>
              </tr>
              <tr>
                <td className={styles.label}>Last Name</td>
                <td>{customer.lastName}</td>
              </tr>
              <tr>
                <td className={styles.label}>Address</td>
                <td>{customer.address}</td>
              </tr>
            </tbody>
          </table>

          <h2 className={styles.sectionTitle}>Credit Profile</h2>
          <table className={styles.table}>
            <tbody>
              <tr>
                <td className={styles.label}>Full Credit Balance</td>
                <td>${customer.fullCreditBalance.toFixed(2)}</td>
              </tr>
              <tr>
                <td className={styles.label}>Spend Balance</td>
                <td>${customer.spendBalance.toFixed(2)}</td>
              </tr>
              <tr>
                <td className={styles.label}>Interest Rate</td>
                <td>{customer.interestRate}%</td>
              </tr>
            </tbody>
          </table>
        </div>
      )}
    </main>
  );
}
