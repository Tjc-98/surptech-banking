/**
 * MongoDB Client
 *
 * Singleton connection with pooling and graceful shutdown.
 * @see https://www.mongodb.com/docs/drivers/node/current/
 */

import { MongoClient, Db } from 'mongodb';
import { mongoConfig, validateMongoDBConfig } from '@/cloud-integration/config/mongodb.config';

let client: MongoClient | null = null;
let clientPromise: Promise<MongoClient> | null = null;

export async function getMongoClient(): Promise<MongoClient> {
  if (client && client.topology && client.topology.isConnected()) {
    return client;
  }

  if (clientPromise) return clientPromise;

  validateMongoDBConfig();

  clientPromise = MongoClient.connect(mongoConfig.uri, mongoConfig.options)
    .then((connected) => {
      client = connected;
      console.log('MongoDB connected successfully');
      return connected;
    })
    .catch((error) => {
      console.error('MongoDB connection error:', error);
      clientPromise = null;
      throw error;
    });

  return clientPromise;
}

export async function getMongoDb(dbName?: string): Promise<Db> {
  const mongoClient = await getMongoClient();
  return mongoClient.db(dbName || mongoConfig.dbName);
}

export async function closeMongoConnection(): Promise<void> {
  if (client) {
    await client.close();
    client = null;
    clientPromise = null;
    console.log('MongoDB connection closed');
  }
}

export function isMongoConnected(): boolean {
  return client !== null && client.topology !== undefined && client.topology.isConnected();
}

export async function pingMongo(): Promise<boolean> {
  try {
    const db = await getMongoDb();
    await db.admin().ping();
    return true;
  } catch (error) {
    console.error('MongoDB ping failed:', error);
    return false;
  }
}

if (typeof process !== 'undefined') {
  process.on('SIGINT', async () => { await closeMongoConnection(); process.exit(0); });
  process.on('SIGTERM', async () => { await closeMongoConnection(); process.exit(0); });
}
