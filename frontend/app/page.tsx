'use client';

import { useState } from 'react';
import styles from './page.module.css';

const API = 'http://localhost:8080/api';

interface Transaction {
  id: number;
  type: 'DEPOSIT' | 'WITHDRAWAL';
  amount: number;
  description: string;
  createdAt: string;
}

interface CustomerInfo {
  socialSecurityNumber: string;
  firstName: string;
  lastName: string;
  address: string;
  fullCreditBalance: number;
  spendBalance: number;
  availableBalance: number;
  interestRate: number;
  transactions: Transaction[];
}

type Tab = 'lookup' | 'register';

export default function HomePage() {
  const [activeTab, setActiveTab] = useState<Tab>('lookup');

  // --- Lookup state ---
  const [ssn, setSsn] = useState('');
  const [customer, setCustomer] = useState<CustomerInfo | null>(null);
  const [lookupError, setLookupError] = useState('');
  const [lookupLoading, setLookupLoading] = useState(false);

  // --- Add transaction state ---
  const [txType, setTxType] = useState<'DEPOSIT' | 'WITHDRAWAL'>('DEPOSIT');
  const [txAmount, setTxAmount] = useState('');
  const [txDescription, setTxDescription] = useState('');
  const [txError, setTxError] = useState('');
  const [txSuccess, setTxSuccess] = useState('');
  const [txLoading, setTxLoading] = useState(false);

  // --- Register state ---
  const [regSsn, setRegSsn] = useState('');
  const [regFirstName, setRegFirstName] = useState('');
  const [regLastName, setRegLastName] = useState('');
  const [regAddress, setRegAddress] = useState('');
  const [regError, setRegError] = useState('');
  const [regSuccess, setRegSuccess] = useState('');
  const [regLoading, setRegLoading] = useState(false);

  // --- Lookup handler ---
  async function handleSearch(e: React.FormEvent) {
    e.preventDefault();
    setLookupError('');
    setCustomer(null);
    setTxError('');
    setTxSuccess('');

    if (!ssn.trim()) {
      setLookupError('Please enter a Social Security Number.');
      return;
    }

    setLookupLoading(true);
    try {
      const response = await fetch(
        `${API}/customer/info?socialSecurityNumber=${encodeURIComponent(ssn)}`
      );

      if (response.status === 404) {
        setLookupError(`No customer found for SSN: ${ssn}`);
      } else if (!response.ok) {
        const body = await response.json().catch(() => ({}));
        setLookupError(body.error || 'Something went wrong. Please try again.');
      } else {
        setCustomer(await response.json());
      }
    } catch {
      setLookupError('Could not connect to the server. Make sure the backend is running on port 8080.');
    } finally {
      setLookupLoading(false);
    }
  }

  // --- Add transaction handler ---
  async function handleAddTransaction(e: React.FormEvent) {
    e.preventDefault();
    setTxError('');
    setTxSuccess('');

    if (!txAmount || Number(txAmount) <= 0) {
      setTxError('Amount must be greater than zero.');
      return;
    }

    setTxLoading(true);
    try {
      const response = await fetch(`${API}/transaction/add`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          socialSecurityNumber: customer!.socialSecurityNumber,
          type: txType,
          amount: Number(txAmount),
          description: txDescription,
        }),
      });

      if (!response.ok) {
        const body = await response.json().catch(() => ({}));
        setTxError(body.error || 'Failed to record transaction.');
      } else {
        setTxSuccess(`${txType.charAt(0) + txType.slice(1).toLowerCase()} of $${Number(txAmount).toFixed(2)} recorded.`);
        setTxAmount('');
        setTxDescription('');
        // Refresh customer data to show the new transaction
        const refreshed = await fetch(
          `${API}/customer/info?socialSecurityNumber=${encodeURIComponent(customer!.socialSecurityNumber)}`
        );
        if (refreshed.ok) setCustomer(await refreshed.json());
      }
    } catch {
      setTxError('Could not connect to the server.');
    } finally {
      setTxLoading(false);
    }
  }

  // --- Register handler ---
  async function handleRegister(e: React.FormEvent) {
    e.preventDefault();
    setRegError('');
    setRegSuccess('');

    setRegLoading(true);
    try {
      const response = await fetch(`${API}/customer/create`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          socialSecurityNumber: regSsn,
          firstName: regFirstName,
          lastName: regLastName,
          address: regAddress,
        }),
      });

      if (!response.ok) {
        const body = await response.json().catch(() => ({}));
        setRegError(body.error || 'Failed to register customer.');
      } else {
        setRegSuccess(`Customer ${regFirstName} ${regLastName} registered successfully.`);
        setRegSsn('');
        setRegFirstName('');
        setRegLastName('');
        setRegAddress('');
      }
    } catch {
      setRegError('Could not connect to the server.');
    } finally {
      setRegLoading(false);
    }
  }

  return (
    <main className={styles.main}>
      <h1 className={styles.title}>SurpTech Banking</h1>

      {/* Tabs */}
      <div className={styles.tabs}>
        <button
          className={`${styles.tab} ${activeTab === 'lookup' ? styles.tabActive : ''}`}
          onClick={() => setActiveTab('lookup')}
        >
          Look Up Customer
        </button>
        <button
          className={`${styles.tab} ${activeTab === 'register' ? styles.tabActive : ''}`}
          onClick={() => setActiveTab('register')}
        >
          Register Customer
        </button>
      </div>

      {/* Lookup tab */}
      {activeTab === 'lookup' && (
        <>
          <div className={styles.card}>
            <form onSubmit={handleSearch}>
              <label className={styles.fieldLabel} htmlFor="ssn">
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
              <button className={styles.button} type="submit" disabled={lookupLoading}>
                {lookupLoading ? 'Searching...' : 'Look Up'}
              </button>
            </form>
            {lookupError && <p className={styles.error}>{lookupError}</p>}
            <p className={styles.hint}>Try: <code>123-45-6789</code> or <code>987-65-4321</code></p>
          </div>

          {customer && (
            <>
              {/* Customer profile */}
              <div className={styles.card}>
                <h2 className={styles.sectionTitle}>Customer Profile</h2>
                <table className={styles.table}>
                  <tbody>
                    <tr><td className={styles.fieldLabel}>SSN</td><td>{customer.socialSecurityNumber}</td></tr>
                    <tr><td className={styles.fieldLabel}>First Name</td><td>{customer.firstName}</td></tr>
                    <tr><td className={styles.fieldLabel}>Last Name</td><td>{customer.lastName}</td></tr>
                    <tr><td className={styles.fieldLabel}>Address</td><td>{customer.address}</td></tr>
                  </tbody>
                </table>

                <h2 className={styles.sectionTitle}>Credit Profile</h2>
                <table className={styles.table}>
                  <tbody>
                    <tr><td className={styles.fieldLabel}>Full Credit Balance</td><td>${customer.fullCreditBalance.toFixed(2)}</td></tr>
                    <tr><td className={styles.fieldLabel}>Spend Balance</td><td>${customer.spendBalance.toFixed(2)}</td></tr>
                    <tr>
                      <td className={styles.fieldLabel}>Available Balance</td>
                      <td className={styles.available}>${customer.availableBalance.toFixed(2)}</td>
                    </tr>
                    <tr><td className={styles.fieldLabel}>Interest Rate</td><td>{customer.interestRate}%</td></tr>
                  </tbody>
                </table>
              </div>

              {/* Add transaction */}
              <div className={styles.card}>
                <h2 className={styles.sectionTitle}>Add Transaction</h2>
                <form onSubmit={handleAddTransaction}>
                  <div className={styles.row}>
                    <div className={styles.field}>
                      <label className={styles.fieldLabel} htmlFor="txType">Type</label>
                      <select
                        id="txType"
                        className={styles.input}
                        value={txType}
                        onChange={(e) => setTxType(e.target.value as 'DEPOSIT' | 'WITHDRAWAL')}
                      >
                        <option value="DEPOSIT">Deposit</option>
                        <option value="WITHDRAWAL">Withdrawal</option>
                      </select>
                    </div>
                    <div className={styles.field}>
                      <label className={styles.fieldLabel} htmlFor="txAmount">Amount ($)</label>
                      <input
                        id="txAmount"
                        className={styles.input}
                        type="number"
                        min="0.01"
                        step="0.01"
                        value={txAmount}
                        onChange={(e) => setTxAmount(e.target.value)}
                        placeholder="0.00"
                      />
                    </div>
                  </div>
                  <label className={styles.fieldLabel} htmlFor="txDesc">Description (optional)</label>
                  <input
                    id="txDesc"
                    className={styles.input}
                    type="text"
                    value={txDescription}
                    onChange={(e) => setTxDescription(e.target.value)}
                    placeholder="e.g. Salary payment"
                  />
                  <button className={styles.button} type="submit" disabled={txLoading}>
                    {txLoading ? 'Recording...' : 'Record Transaction'}
                  </button>
                </form>
                {txError && <p className={styles.error}>{txError}</p>}
                {txSuccess && <p className={styles.success}>{txSuccess}</p>}
              </div>

              {/* Transaction history */}
              <div className={styles.card}>
                <h2 className={styles.sectionTitle}>Transaction History</h2>
                {customer.transactions.length === 0 ? (
                  <p className={styles.hint}>No transactions yet.</p>
                ) : (
                  <table className={styles.table}>
                    <thead>
                      <tr>
                        <th className={styles.th}>Date</th>
                        <th className={styles.th}>Type</th>
                        <th className={styles.th}>Amount</th>
                        <th className={styles.th}>Description</th>
                      </tr>
                    </thead>
                    <tbody>
                      {customer.transactions.map((tx) => (
                        <tr key={tx.id}>
                          <td>{new Date(tx.createdAt).toLocaleDateString()}</td>
                          <td className={tx.type === 'DEPOSIT' ? styles.deposit : styles.withdrawal}>
                            {tx.type}
                          </td>
                          <td>${tx.amount.toFixed(2)}</td>
                          <td>{tx.description || '-'}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                )}
              </div>
            </>
          )}
        </>
      )}

      {/* Register tab */}
      {activeTab === 'register' && (
        <div className={styles.card}>
          <h2 className={styles.sectionTitle}>Register New Customer</h2>
          <form onSubmit={handleRegister}>
            <label className={styles.fieldLabel} htmlFor="regSsn">Social Security Number</label>
            <input
              id="regSsn"
              className={styles.input}
              type="text"
              value={regSsn}
              onChange={(e) => setRegSsn(e.target.value)}
              placeholder="XXX-XX-XXXX"
              maxLength={11}
            />

            <div className={styles.row}>
              <div className={styles.field}>
                <label className={styles.fieldLabel} htmlFor="regFirst">First Name</label>
                <input
                  id="regFirst"
                  className={styles.input}
                  type="text"
                  value={regFirstName}
                  onChange={(e) => setRegFirstName(e.target.value)}
                  placeholder="First name"
                />
              </div>
              <div className={styles.field}>
                <label className={styles.fieldLabel} htmlFor="regLast">Last Name</label>
                <input
                  id="regLast"
                  className={styles.input}
                  type="text"
                  value={regLastName}
                  onChange={(e) => setRegLastName(e.target.value)}
                  placeholder="Last name"
                />
              </div>
            </div>

            <label className={styles.fieldLabel} htmlFor="regAddress">Address</label>
            <input
              id="regAddress"
              className={styles.input}
              type="text"
              value={regAddress}
              onChange={(e) => setRegAddress(e.target.value)}
              placeholder="Full address"
            />

            <button className={styles.button} type="submit" disabled={regLoading}>
              {regLoading ? 'Registering...' : 'Register Customer'}
            </button>
          </form>

          {regError && <p className={styles.error}>{regError}</p>}
          {regSuccess && <p className={styles.success}>{regSuccess}</p>}
        </div>
      )}
    </main>
  );
}
