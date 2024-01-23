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

   
</div>