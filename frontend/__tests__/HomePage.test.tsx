import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom';
import HomePage from '../app/page';

// Mock the global fetch function
global.fetch = jest.fn();

describe('HomePage', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders the search form', () => {
    render(<HomePage />);
    expect(screen.getByText('SurpTech Banking')).toBeInTheDocument();
    expect(screen.getByLabelText('Social Security Number')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: 'Look Up' })).toBeInTheDocument();
  });

  it('shows an error when the search field is empty', async () => {
    render(<HomePage />);
    fireEvent.click(screen.getByRole('button', { name: 'Look Up' }));
    expect(await screen.findByText('Please enter a Social Security Number.')).toBeInTheDocument();
  });

  it('shows customer data on a successful lookup', async () => {
    (global.fetch as jest.Mock).mockResolvedValueOnce({
      ok: true,
      status: 200,
      json: async () => ({
        socialSecurityNumber: '123-45-6789',
        firstName: 'James',
        lastName: 'Smith',
        address: '456 Tailor Street, California, LA 56001',
        fullCreditBalance: 15000,
        spendBalance: 5000,
        interestRate: 3.5,
      }),
    });

    render(<HomePage />);
    fireEvent.change(screen.getByLabelText('Social Security Number'), {
      target: { value: '123-45-6789' },
    });
    fireEvent.click(screen.getByRole('button', { name: 'Look Up' }));

    await waitFor(() => {
      expect(screen.getByText('James')).toBeInTheDocument();
      expect(screen.getByText('Smith')).toBeInTheDocument();
      expect(screen.getByText('$15000.00')).toBeInTheDocument();
    });
  });

  it('shows a not-found error for an unknown SSN', async () => {
    (global.fetch as jest.Mock).mockResolvedValueOnce({
      ok: false,
      status: 404,
    });

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
      expect(
        screen.getByText(/Could not connect to the server/)
      ).toBeInTheDocument();
    });
  });
});
