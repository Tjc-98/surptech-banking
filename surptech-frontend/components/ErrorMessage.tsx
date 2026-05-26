'use client';

import { ApiError } from '@/domain/error/ApiError';
import { Icon } from '@/components/ui/Icon';

interface ErrorMessageProps {
  error: ApiError | Error | string;
  onDismiss?: () => void;
}

const ERROR_MESSAGES = {
  notFound: 'Customer not found. Please check the Social Security Number and try again.',
  connection: 'Unable to connect to the server. Please ensure the data aggregator service is running.',
  server: 'A server error occurred. Please try again later.',
  default: 'An unexpected error occurred',
} as const;

const ERROR_ICONS = {
  notFound: '🔍',
  connection: '🔌',
  default: '⚠️',
} as const;

export const ErrorMessage: React.FC<ErrorMessageProps> = ({ error, onDismiss }) => {
  const getMessage = (): string => {
    if (typeof error === 'string') return error;
    
    if (error instanceof ApiError) {
      if (error.isNotFound()) return ERROR_MESSAGES.notFound;
      if (error.statusCode === 0) return ERROR_MESSAGES.connection;
      if (error.isServerError()) return ERROR_MESSAGES.server;
      return error.message;
    }
    
    return error.message || ERROR_MESSAGES.default;
  };
  
  const getIcon = (): string => {
    if (error instanceof ApiError) {
      if (error.isNotFound()) return ERROR_ICONS.notFound;
      if (error.statusCode === 0) return ERROR_ICONS.connection;
    }
    return ERROR_ICONS.default;
  };
  
  const statusCode = error instanceof ApiError && error.statusCode > 0 ? error.statusCode : null;
  
  return (
    <div className="w-full max-w-4xl bg-red-50 border-2 border-red-200 rounded-lg p-6">
      <div className="flex items-start gap-4">
        <div className="flex-shrink-0 text-3xl">{getIcon()}</div>
        <div className="flex-1">
          <h3 className="text-lg font-semibold text-red-800 mb-2">Error</h3>
          <p className="text-red-700">{getMessage()}</p>
          {statusCode && (
            <p className="text-sm text-red-600 mt-2">Status Code: {statusCode}</p>
          )}
        </div>
        {onDismiss && (
          <button
            onClick={onDismiss}
            className="flex-shrink-0 text-red-400 hover:text-red-600 transition-colors"
            aria-label="Dismiss error"
          >
            <Icon name="close" className="w-6 h-6" />
          </button>
        )}
      </div>
    </div>
  );
};
