<script>
   import { goto } from '$app/navigation';

  export let cartData = [];

   function goBack() {
      window.history.back();
   }

   function goToCart() {
      goto(`/cart`, true);
   }

 </script>
 
<div class="flex m-4">
   <button on:click={goBack} class="mr-2 ml-2 bg-white text-gray-700 px-3 py-2 rounded-full flex items-center">
      <svg xmlns="http://www.w3.org/2000/svg" height="1em" viewBox="0 0 448 512">
         <path fill="#6b7280" d="M9.4 233.4c-12.5 12.5-12.5 32.8 0 45.3l192 192c12.5 12.5 32.8 12.5 45.3 0s12.5-32.8 0-45.3L77.3 256 246.6 86.6c12.5-12.5 12.5-32.8 0-45.3s-32.8-12.5-45.3 0l-192 192z"/>
      </svg>
      Retour
   </button>

   <button on:click={goToCart} class="ml-auto mt-2 mr-2 bg-green-500 text-white rounded-full px-3 py-3 flex items-center">
      <svg xmlns="http://www.w3.org/2000/svg" height="1em" viewBox="0 0 576 512">
         <path fill="#ffffff" d="M0 24C0 10.7 10.7 0 24 0H69.5c22 0 41.5 12.8 50.6 32h411c26.3 0 45.5 25 38.6 50.4l-41 152.3c-8.5 31.4-37 53.3-69.5 53.3H170.7l5.4 28.5c2.2 11.3 12.1 19.5 23.6 19.5H488c13.3 0 24 10.7 24 24s-10.7 24-24 24H199.7c-34.6 0-64.3-24.6-70.7-58.5L77.4 54.5c-.7-3.8-4-6.5-7.9-6.5H24C10.7 48 0 37.3 0 24zM128 464a48 48 0 1 1 96 0 48 48 0 1 1 -96 0zm336-48a48 48 0 1 1 0 96 48 48 0 1 1 0-96z"/>
      </svg>
      {#if cartData.length > 0}
      <span class="absolute top-4 right-3 bg-red-500 text-white rounded-full px-2 py-1 text-xs">{cartData.length}</span>
      {/if}
   </button>
</div>