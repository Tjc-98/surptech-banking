/**
 * MongoDB Configuration
 *
 * All sensitive values are loaded from environment variables.
 * @see https://www.mongodb.com/docs/drivers/node/current/
 */

export interface MongoDBConfig {
  uri: string;
  dbName: string;
  options?: {
    maxPoolSize?: number;
    minPoolSize?: number;
    maxIdleTimeMS?: number;
    serverSelectionTimeoutMS?: number;
    socketTimeoutMS?: number;
  };
}

export const mongoConfig: MongoDBConfig = {
  uri: process.env.MONGODB_URI || '',
  dbName: process.env.MONGODB_DB_NAME || 'surptech',
  options: {
    maxPoolSize: parseInt(process.env.MONGODB_MAX_POOL_SIZE || '10'),
    minPoolSize: parseInt(process.env.MONGODB_MIN_POOL_SIZE || '2'),
    maxIdleTimeMS: parseInt(process.env.MONGODB_MAX_IDLE_TIME_MS || '60000'),
    serverSelectionTimeoutMS: parseInt(
      process.env.MONGODB_SERVER_SELECTION_TIMEOUT_MS || '5000'
    ),
    socketTimeoutMS: parseInt(process.env.MONGODB_SOCKET_TIMEOUT_MS || '45000'),
  },
};

export function validateMongoDBConfig(): void {
  if (!mongoConfig.uri) {
    throw new Error('Missing required MongoDB configuration: uri');
  }
  if (!mongoConfig.dbName) {
    throw new Error('Missing required MongoDB configuration: dbName');
  }
}

export function isMongoDBConfigured(): boolean {
  try {
    validateMongoDBConfig();
    return true;
  } catch {
    return false;
  }
}
