/**
 * MongoDB Customer by ID Handler
 *
 * GET    /api/cloud/mongodb/customers/[id]
 * PATCH  /api/cloud/mongodb/customers/[id]
 * DELETE /api/cloud/mongodb/customers/[id]
 *
 * Registered via app/api/cloud/mongodb/customers/[id]/route.ts (Next.js requirement).
 */

import { NextRequest, NextResponse } from 'next/server';
import { CustomerRepository } from '@/cloud-integration/lib/mongodb/repositories/CustomerRepository';

const repository = new CustomerRepository();

export async function GET(
  _request: NextRequest,
  { params }: { params: { id: string } }
) {
  try {
    const customer = await repository.findById(params.id);

    if (!customer) {
      return NextResponse.json({ error: 'Customer not found' }, { status: 404 });
    }

    return NextResponse.json({
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
    });
  } catch (error) {
    console.error('Error fetching customer:', error);
    return NextResponse.json({ error: 'Failed to fetch customer' }, { status: 500 });
  }
}

export async function PATCH(
  request: NextRequest,
  { params }: { params: { id: string } }
) {
  try {
    const body = await request.json();
    delete body._id;
    delete body.createdAt;
    body.updatedAt = new Date();

    const customer = await repository.updateById(params.id, { $set: body });

    if (!customer) {
      return NextResponse.json({ error: 'Customer not found' }, { status: 404 });
    }

    return NextResponse.json({
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
    });
  } catch (error) {
    console.error('Error updating customer:', error);
    return NextResponse.json({ error: 'Failed to update customer' }, { status: 500 });
  }
}

export async function DELETE(
  _request: NextRequest,
  { params }: { params: { id: string } }
) {
  try {
    const deleted = await repository.deleteById(params.id);

    if (!deleted) {
      return NextResponse.json({ error: 'Customer not found' }, { status: 404 });
    }

    return NextResponse.json({ success: true, message: 'Customer deleted successfully' });
  } catch (error) {
    console.error('Error deleting customer:', error);
    return NextResponse.json({ error: 'Failed to delete customer' }, { status: 500 });
  }
}
