// store.js
import { writable } from 'svelte/store';

export const cart = writable([
  { name: "Produit 1", quantity: 2, price: 15.99 },
  { name: "Produit 2", quantity: 1, price: 9.99 },
  // Add other products to the cart
]);

export const total = writable(0);
