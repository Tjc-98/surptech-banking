/**
 * MongoDB Customers Handler
 *
 * GET  /api/cloud/mongodb/customers  — list / search
 * POST /api/cloud/mongodb/customers  — create
 *
 * Registered via app/api/cloud/mongodb/customers/route.ts (Next.js requirement).
 */

import { NextRequest, NextResponse } from 'next/server';
import { CustomerRepository } from '@/cloud-integration/lib/mongodb/repositories/CustomerRepository';

const repository = new CustomerRepository();

export async function GET(request: NextRequest) {
  try {
    const searchParams = request.nextUrl.searchParams;
    const firstName = searchParams.get('firstName');
    const lastName = searchParams.get('lastName');
    const ssn = searchParams.get('ssn');

    let customers;

    if (ssn) {
      const customer = await repository.findBySSN(ssn);
      customers = customer ? [customer] : [];
    } else if (firstName || lastName) {
      customers = await repository.findByName(
        firstName || undefined,
        lastName || undefined
      );
    } else {
      customers = await repository.find({}, { limit: 100 });
    }

    return NextResponse.json({
      success: true,
      count: customers.length,
      customers: customers.map((c) => ({
        id: c._id?.toString(),
        socialSecurityNumber: c.socialSecurityNumber,
        firstName: c.firstName,
        lastName: c.lastName,
        address: c.address,
        fullCreditBalance: c.fullCreditBalance,
        spendBalance: c.spendBalance,
        interestRate: c.interestRate,
        createdAt: c.createdAt,
        updatedAt: c.updatedAt,
      })),
    });
  } catch (error) {
    console.error('Error fetching customers:', error);
    return NextResponse.json({ error: 'Failed to fetch customers' }, { status: 500 });
  }
}

export async function POST(request: NextRequest) {
  try {
    const body = await request.json();

    const requiredFields = [
      'socialSecurityNumber',
      'firstName',
      'lastName',
      'address',
      'fullCreditBalance',
      'spendBalance',
      'interestRate',
    ];

    for (const field of requiredFields) {
      if (!body[field] && body[field] !== 0) {
        return NextResponse.json(
          { error: `Missing required field: ${field}` },
          { status: 400 }
        );
      }
    }

    const existing = await repository.findBySSN(body.socialSecurityNumber);
    if (existing) {
      return NextResponse.json(
        { error: 'Customer with this SSN already exists' },
        { status: 409 }
      );
    }

    const customer = await repository.create({
      socialSecurityNumber: body.socialSecurityNumber,
      firstName: body.firstName,
      lastName: body.lastName,
      address: body.address,
      fullCreditBalance: body.fullCreditBalance,
      spendBalance: body.spendBalance,
      interestRate: body.interestRate,
      createdAt: new Date(),
      updatedAt: new Date(),
    });

    return NextResponse.json(
      {
        success: true,
        customer: {
          id: customer._id?.toString(),
          socialSecurityNumber: customer.socialSecurityNumber,
          firstName: customer.firstName,
          lastName: customer.lastName,
          address: customer.address,
          fullCreditBalance: customer.fullCreditBalance,
          spendBalance: customer.spendBalance,
          interestRate: customer.interestRate,
          createdAt: customer.createdAt,
          updatedAt: customer.updatedAt,
        },
      },
      { status: 201 }
    );
  } catch (error) {
    console.error('Error creating customer:', error);
    return NextResponse.json({ error: 'Failed to create customer' }, { status: 500 });
  }
}
