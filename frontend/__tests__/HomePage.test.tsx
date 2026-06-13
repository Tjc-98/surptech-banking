import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom';
import HomePage from '../app/page';

global.fetch = jest.fn();

const mockCustomer = {
  socialSecurityNumber: '123-45-6789',
  firstName: 'James',
  lastName: 'Smith',
  address: '456 Tailor Street, California, LA 56001',
  fullCreditBalance: 15000,
  spendBalance: 5000,
  availableBalance: 10000,
  interestRate: 3.5,
  transactions: [
    { id: 1, type: 'DEPOSIT', amount: 2000, description: 'Salary', createdAt: '2026-05-01T09:00:00' },
    { id: 2, type: 'WITHDRAWAL', amount: 500, description: 'Groceries', createdAt: '2026-05-05T14:00:00' },
  ],
};

describe('HomePage - Lookup tab', () => {
  beforeEach(() => jest.clearAllMocks());

  it('renders the search form and both tabs', () => {
    render(<HomePage />);
    expect(screen.getByText('SurpTech Banking')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: 'Look Up Customer' })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: 'Register Customer' })).toBeInTheDocument();
    expect(screen.getByLabelText('Social Security Number')).toBeInTheDocument();
  });

  it('shows an error when search field is empty', async () => {
    render(<HomePage />);
    fireEvent.click(screen.getByRole('button', { name: 'Look Up' }));
    expect(await screen.findByText('Please enter a Social Security Number.')).toBeInTheDocument();
  });

  it('shows customer data and available balance on successful lookup', async () => {
    (global.fetch as jest.Mock).mockResolvedValueOnce({
      ok: true,
      status: 200,
      json: async () => mockCustomer,
    });

    render(<HomePage />);
    fireEvent.change(screen.getByLabelText('Social Security Number'), {
      target: { value: '123-45-6789' },
    });
    fireEvent.click(screen.getByRole('button', { name: 'Look Up' }));

    await waitFor(() => {
      expect(screen.getByText('James')).toBeInTheDocument();
      expect(screen.getByText('$10000.00')).toBeInTheDocument(); // available balance
      expect(screen.getByText('DEPOSIT')).toBeInTheDocument();
      expect(screen.getByText('WITHDRAWAL')).toBeInTheDocument();
    });
  });

  it('shows a not-found error for an unknown SSN', async () => {
    (global.fetch as jest.Mock).mockResolvedValueOnce({ ok: false, status: 404 });

    render(<HomePage />);
    fireEvent.change(screen.getByLabelText('Social Security Number'), {
      target: { value: '000-00-0000' },
    });
    fireEvent.click(screen.getByRole('button', { name: 'Look Up' }));

    await waitFor(() => {
      expect(screen.getByText('No customer found for SSN: 000-00-0000')).toBeInTheDocument();
    });
  });

  it('shows a connection error when fetch throws', async () => {
    (global.fetch as jest.Mock).mockRejectedValueOnce(new Error('Network error'));

    render(<HomePage />);
    fireEvent.change(screen.getByLabelText('Social Security Number'), {
      target: { value: '123-45-6789' },
    });
    fireEvent.click(screen.getByRole('button', { name: 'Look Up' }));

    await waitFor(() => {
      expect(screen.getByText(/Could not connect to the server/)).toBeInTheDocument();
    });
  });
});

describe('HomePage - Register tab', () => {
  beforeEach(() => jest.clearAllMocks());

  it('switches to register tab and shows the form', () => {
    render(<HomePage />);
    fireEvent.click(screen.getByRole('button', { name: 'Register Customer' }));
    expect(screen.getByText('Register New Customer')).toBeInTheDocument();
    expect(screen.getByLabelText('First Name')).toBeInTheDocument();
    expect(screen.getByLabelText('Last Name')).toBeInTheDocument();
    expect(screen.getByLabelText('Address')).toBeInTheDocument();
  });

  it('shows success message on successful registration', async () => {
    (global.fetch as jest.Mock).mockResolvedValueOnce({
      ok: true,
      status: 201,
      json: async () => ({
        socialSecurityNumber: '111-22-3333',
        firstName: 'Alice',
        lastName: 'Johnson',
      }),
    });

    render(<HomePage />);
    fireEvent.click(screen.getByRole('button', { name: 'Register Customer' }));

    fireEvent.change(screen.getByLabelText('Social Security Number'), {
      target: { value: '111-22-3333' },
    });
    fireEvent.change(screen.getByLabelText('First Name'), { target: { value: 'Alice' } });
    fireEvent.change(screen.getByLabelText('Last Name'), { target: { value: 'Johnson' } });
    fireEvent.change(screen.getByLabelText('Address'), { target: { value: '789 Oak Ave' } });
    fireEvent.click(screen.getByRole('button', { name: 'Register Customer' }));

    await waitFor(() => {
      expect(screen.getByText('Customer Alice Johnson registered successfully.')).toBeInTheDocument();
    });
  });

  it('shows error message when registration fails', async () => {
    (global.fetch as jest.Mock).mockResolvedValueOnce({
      ok: false,
      status: 400,
      json: async () => ({ error: 'First name is required.' }),
    });

    render(<HomePage />);
    fireEvent.click(screen.getByRole('button', { name: 'Register Customer' }));
    fireEvent.click(screen.getByRole('button', { name: 'Register Customer' }));

    await waitFor(() => {
      expect(screen.getByText('First name is required.')).toBeInTheDocument();
    });
  });
});
