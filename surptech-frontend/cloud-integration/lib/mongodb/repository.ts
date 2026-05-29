/**
 * MongoDB Base Repository
 *
 * Generic CRUD operations — extend this for each collection.
 * @see https://www.mongodb.com/docs/drivers/node/current/usage-examples/
 */

import {
  Collection,
  Document,
  Filter,
  OptionalId,
  UpdateFilter,
  FindOptions,
  ObjectId,
} from 'mongodb';
import { getMongoDb } from './client';

export abstract class BaseRepository<T extends Document> {
  protected collectionName: string;

  constructor(collectionName: string) {
    this.collectionName = collectionName;
  }

  protected async getCollection(): Promise<Collection<T>> {
    const db = await getMongoDb();
    return db.collection<T>(this.collectionName);
  }

  async findById(id: string | ObjectId): Promise<T | null> {
    const collection = await this.getCollection();
    const objectId = typeof id === 'string' ? new ObjectId(id) : id;
    return await collection.findOne({ _id: objectId } as Filter<T>);
  }

  async find(filter: Filter<T> = {}, options?: FindOptions): Promise<T[]> {
    const collection = await this.getCollection();
    return await collection.find(filter, options).toArray();
  }

  async findOne(filter: Filter<T>): Promise<T | null> {
    const collection = await this.getCollection();
    return await collection.findOne(filter);
  }

  async create(document: OptionalId<T>): Promise<T> {
    const collection = await this.getCollection();
    const result = await collection.insertOne(document);
    return { ...document, _id: result.insertedId } as T;
  }

  async createMany(documents: OptionalId<T>[]): Promise<T[]> {
    const collection = await this.getCollection();
    const result = await collection.insertMany(documents);
    return documents.map((doc, index) => ({
      ...doc,
      _id: result.insertedIds[index],
    })) as T[];
  }

  async updateById(
    id: string | ObjectId,
    update: UpdateFilter<T>
  ): Promise<T | null> {
    const collection = await this.getCollection();
    const objectId = typeof id === 'string' ? new ObjectId(id) : id;
    const result = await collection.findOneAndUpdate(
      { _id: objectId } as Filter<T>,
      update,
      { returnDocument: 'after' }
    );
    return result || null;
  }

  async updateMany(filter: Filter<T>, update: UpdateFilter<T>): Promise<number> {
    const collection = await this.getCollection();
    const result = await collection.updateMany(filter, update);
    return result.modifiedCount;
  }

  async deleteById(id: string | ObjectId): Promise<boolean> {
    const collection = await this.getCollection();
    const objectId = typeof id === 'string' ? new ObjectId(id) : id;
    const result = await collection.deleteOne({ _id: objectId } as Filter<T>);
    return result.deletedCount > 0;
  }

  async deleteMany(filter: Filter<T>): Promise<number> {
    const collection = await this.getCollection();
    const result = await collection.deleteMany(filter);
    return result.deletedCount;
  }

  async count(filter: Filter<T> = {}): Promise<number> {
    const collection = await this.getCollection();
    return await collection.countDocuments(filter);
  }

  async exists(filter: Filter<T>): Promise<boolean> {
    return (await this.count(filter)) > 0;
  }

  /** WARNING: deletes every document in the collection */
  async deleteAll(): Promise<number> {
    const collection = await this.getCollection();
    const result = await collection.deleteMany({});
    return result.deletedCount;
  }
}
