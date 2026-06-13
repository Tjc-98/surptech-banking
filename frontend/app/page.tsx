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
      setLookupError("Please enter a customer's SSN to search.");
      return;
    }

    setLookupLoading(true);
    try {
      const response = await fetch(
        `${API}/customer/info?socialSecurityNumber=${encodeURIComponent(ssn)}`
      );

      if (response.status === 404) {
        setLookupError(`We couldn't find anyone with SSN ${ssn}. Double-check the number and try again.`);
      } else if (!response.ok) {
        const body = await response.json().catch(() => ({}));
        setLookupError(body.error || 'Something went wrong. Please try again in a moment.');
      } else {
        setCustomer(await response.json());
      }
    } catch {
      setLookupError("Can't reach the server right now. Make sure the backend is running on port 8080.");
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
      setTxError('The amount needs to be more than $0.00.');
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
        setTxError(body.error || "Couldn't record the transaction. Please try again.");
      } else {
        const label = txType === 'DEPOSIT' ? 'Deposit' : 'Withdrawal';
        setTxSuccess(`${label} of $${Number(txAmount).toFixed(2)} recorded successfully.`);
        setTxAmount('');
        setTxDescription('');
        // Refresh so the new transaction shows up in the history
        const refreshed = await fetch(
          `${API}/customer/info?socialSecurityNumber=${encodeURIComponent(customer!.socialSecurityNumber)}`
        );
        if (refreshed.ok) setCustomer(await refreshed.json());
      }
    } catch {
      setTxError("Can't reach the server right now. Please try again.");
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
        setRegError(body.error || "Couldn't register the customer. Please check the details and try again.");
      } else {
        setRegSuccess(`${regFirstName} ${regLastName} has been added successfully.`);
        setRegSsn('');
        setRegFirstName('');
        setRegLastName('');
        setRegAddress('');
      }
    } catch {
      setRegError("Can't reach the server right now. Please try again.");
    } finally {
      setRegLoading(false);
    }
  }

  return (
    <main className={styles.main}>
      <h1 className={styles.title}>SurpTech Banking</h1>
      <p className={styles.subtitle}>Look up account details or add a new customer below.</p>

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
                Customer SSN
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
                {lookupLoading ? 'Looking up...' : 'Search'}
              </button>
            </form>
            {lookupError && <p className={styles.error}>{lookupError}</p>}
            <p className={styles.hint}>
              Not sure what to try? Use <code>123-45-6789</code> or <code>987-65-4321</code>.
            </p>
          </div>

          {customer && (
            <>
              {/* Customer details */}
              <div className={styles.card}>
                <h2 className={styles.sectionTitle}>
                  {customer.firstName} {customer.lastName}
                </h2>
                <table className={styles.table}>
                  <tbody>
                    <tr><td className={styles.fieldLabel}>SSN</td><td>{customer.socialSecurityNumber}</td></tr>
                    <tr><td className={styles.fieldLabel}>Address</td><td>{customer.address}</td></tr>
                  </tbody>
                </table>

                <h2 className={styles.sectionTitle}>Credit Account</h2>
                <table className={styles.table}>
                  <tbody>
                    <tr><td className={styles.fieldLabel}>Credit limit</td><td>${customer.fullCreditBalance.toFixed(2)}</td></tr>
                    <tr><td className={styles.fieldLabel}>Amount used</td><td>${customer.spendBalance.toFixed(2)}</td></tr>
                    <tr>
                      <td className={styles.fieldLabel}>Available to spend</td>
                      <td className={styles.available}>${customer.availableBalance.toFixed(2)}</td>
                    </tr>
                    <tr><td className={styles.fieldLabel}>Interest rate</td><td>{customer.interestRate}%</td></tr>
                  </tbody>
                </table>
              </div>

              {/* Add transaction */}
              <div className={styles.card}>
                <h2 className={styles.sectionTitle}>Record a Transaction</h2>
                <form onSubmit={handleAddTransaction}>
                  <div className={styles.row}>
                    <div className={styles.field}>
                      <label className={styles.fieldLabel} htmlFor="txType">What kind?</label>
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
                  <label className={styles.fieldLabel} htmlFor="txDesc">Note (optional)</label>
                  <input
                    id="txDesc"
                    className={styles.input}
                    type="text"
                    value={txDescription}
                    onChange={(e) => setTxDescription(e.target.value)}
                    placeholder="e.g. Monthly salary"
                  />
                  <button className={styles.button} type="submit" disabled={txLoading}>
                    {txLoading ? 'Saving...' : 'Save Transaction'}
                  </button>
                </form>
                {txError && <p className={styles.error}>{txError}</p>}
                {txSuccess && <p className={styles.success}>{txSuccess}</p>}
              </div>

              {/* Transaction history */}
              <div className={styles.card}>
                <h2 className={styles.sectionTitle}>Transaction History</h2>
                {customer.transactions.length === 0 ? (
                  <p className={styles.hint}>No transactions on record yet.</p>
                ) : (
                  <table className={styles.table}>
                    <thead>
                      <tr>
                        <th className={styles.th}>Date</th>
                        <th className={styles.th}>Type</th>
                        <th className={styles.th}>Amount</th>
                        <th className={styles.th}>Note</th>
                      </tr>
                    </thead>
                    <tbody>
                      {customer.transactions.map((tx) => (
                        <tr key={tx.id}>
                          <td>{new Date(tx.createdAt).toLocaleDateString()}</td>
                          <td className={tx.type === 'DEPOSIT' ? styles.deposit : styles.withdrawal}>
                            {tx.type === 'DEPOSIT' ? 'Deposit' : 'Withdrawal'}
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
          <h2 className={styles.sectionTitle}>Add a New Customer</h2>
          <p className={styles.hint}>Fill in the details below to create a new account.</p>
          <form onSubmit={handleRegister}>
            <label className={styles.fieldLabel} htmlFor="regSsn">SSN</label>
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
                <label className={styles.fieldLabel} htmlFor="regFirst">First name</label>
                <input
                  id="regFirst"
                  className={styles.input}
                  type="text"
                  value={regFirstName}
                  onChange={(e) => setRegFirstName(e.target.value)}
                  placeholder="e.g. James"
                />
              </div>
              <div className={styles.field}>
                <label className={styles.fieldLabel} htmlFor="regLast">Last name</label>
                <input
                  id="regLast"
                  className={styles.input}
                  type="text"
                  value={regLastName}
                  onChange={(e) => setRegLastName(e.target.value)}
                  placeholder="e.g. Smith"
                />
              </div>
            </div>

            <label className={styles.fieldLabel} htmlFor="regAddress">Home address</label>
            <input
              id="regAddress"
              className={styles.input}
              type="text"
              value={regAddress}
              onChange={(e) => setRegAddress(e.target.value)}
              placeholder="Street, City, State ZIP"
            />

            <button className={styles.button} type="submit" disabled={regLoading}>
              {regLoading ? 'Saving...' : 'Save Customer'}
            </button>
          </form>

          {regError && <p className={styles.error}>{regError}</p>}
          {regSuccess && <p className={styles.success}>{regSuccess}</p>}
        </div>
      )}
    </main>
  );
}
