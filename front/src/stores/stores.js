import { writable } from 'svelte/store';

function createSessionStorage() {
  // Vérifier si le code s'exécute côté client avant d'utiliser localStorage
  const isClient = typeof window !== 'undefined';

  const storedUrl = isClient ? localStorage.getItem('redirectUrl') : null;
  const redirectUrl = writable(storedUrl || null);

  if (isClient) {
    redirectUrl.subscribe(($url) => {
      localStorage.setItem('redirectUrl', $url);
    });
  }
  
  const storedData = isClient ? localStorage.getItem('cartData') : null;
  const data = writable(storedData ? JSON.parse(storedData) : []);

  if (isClient) {
    data.subscribe(($data) => {
      localStorage.setItem('cartData', JSON.stringify($data));
    });
  }

  return data;
}
export const selectedDate = writable(null);

export const sessionStorage = createSessionStorage();