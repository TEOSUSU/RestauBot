import { json } from '@sveltejs/kit'
import Stripe from 'stripe'
import { SECRET_STRIPE_KEY } from '$env/static/private'

const stripe = new Stripe(SECRET_STRIPE_KEY)

export async function POST({ request }) {
  try {
    const body = await request.json();
    
    const amount = parseInt(body.amount, 10);
    
    const paymentIntent = await stripe.paymentIntents.create({
      amount: amount,
      currency: 'eur', 
      automatic_payment_methods: {
        enabled: true
      }
    });

    return json({
      clientSecret: paymentIntent.client_secret
    });
  } catch (error) {
    console.error('Error creating payment intent:', error);
    return new Response(JSON.stringify({ error: error.message }), {
      status: 500,
      headers: {
        'Content-Type': 'application/json'
      }
    });
  }
}