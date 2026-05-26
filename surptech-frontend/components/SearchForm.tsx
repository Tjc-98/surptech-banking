'use client';

import { useState, FormEvent, ChangeEvent } from 'react';
import { Icon } from '@/components/ui/Icon';
import { 
  isValidSocialSecurityNumber, 
  formatSocialSecurityNumber,
  ValidationMessages 
} from '@/utils/validation.utils';

interface SearchFormProps {
  onSearch: (ssn: string) => void;
  disabled?: boolean;
}

export const SearchForm: React.FC<SearchFormProps> = ({ onSearch, disabled = false }) => {
  const [ssn, setSsn] = useState('');
  const [error, setError] = useState('');
  
  const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
    setSsn(e.target.value);
    if (error) setError('');
  };
  
  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    
    if (!ssn.trim()) {
      setError(ValidationMessages.SSN_REQUIRED);
      return;
    }
    
    const formatted = formatSocialSecurityNumber(ssn);
    if (!isValidSocialSecurityNumber(formatted)) {
      setError(ValidationMessages.SSN_INVALID_FORMAT);
      return;
    }
    
    setError('');
    onSearch(formatted);
  };
  
  const inputClasses = `
    w-full px-4 py-3 rounded-lg border-2 
    focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent
    disabled:bg-secondary-100 disabled:cursor-not-allowed
    transition-colors
    ${error ? 'border-red-500 focus:ring-red-500' : 'border-secondary-300'}
  `;
  
  const buttonClasses = `
    w-full px-6 py-3 rounded-lg font-semibold text-white
    transition-all transform
    ${disabled 
      ? 'bg-secondary-400 cursor-not-allowed' 
      : 'bg-primary-600 hover:bg-primary-700 hover:shadow-lg active:scale-95'
    }
  `;
  
  return (
    <form onSubmit={handleSubmit} className="w-full max-w-md space-y-4">
      <div>
        <label htmlFor="ssn-input" className="block text-sm font-medium text-secondary-700 mb-2">
          Social Security Number
        </label>
        <input
          id="ssn-input"
          type="text"
          value={ssn}
          onChange={handleChange}
          placeholder="XXX-XX-XXXX"
          disabled={disabled}
          className={inputClasses}
          maxLength={11}
          autoComplete="off"
        />
        {error && (
          <p className="mt-2 text-sm text-red-600 flex items-center gap-1">
            <Icon name="error" className="w-4 h-4" />
            {error}
          </p>
        )}
      </div>
      
      <button type="submit" disabled={disabled} className={buttonClasses}>
        {disabled ? (
          <span className="flex items-center justify-center gap-3">
            <Icon name="spinner" className="animate-spin h-5 w-5" />
            Searching...
          </span>
        ) : (
          'Search Customer'
        )}
      </button>
    </form>
  );
};
