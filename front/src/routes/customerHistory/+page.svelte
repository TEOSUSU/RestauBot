<script>
    import { page } from '$app/stores';
    import { onMount } from "svelte";
    import { writable } from 'svelte/store';
    import Navbar from './Navbar.svelte';
    let open = false;
    const url = $page.url;
    const customerId = parseInt(url.searchParams.get('customer'));
    const customerApiUrl = `http://localhost:8080/api/purchases/customer/${customerId}`;
    let historyData = [];
onMount(() => {
  fetch(customerApiUrl, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
    .then(response => response.json())
    .then(responseData => {
      console.log(responseData)
     historyData = responseData; // Mettez à jour historyData avec les données de l'API
    })
    .catch(error => {
      console.error("Erreur lors de la récupération des détails de l'historique :", error);
      
    });
    console.log(historyData);
});



</script>
<Navbar/>
<div class="p-4 sm:ml-64 flex flex-col items-center">
  <h1 class="font-bold text-xl pb-5">Historique des commandes</h1>
  <div class="overflow-x-auto shadow-md rounded-lg">
  <table class="table-auto border-collapse ">
    <thead>
      <tr class="bg-gray-300">
        <th class="px-4 py-2">Date</th>
        <th class="px-4 py-2">Restaurant</th>
        <th class="px-4 py-2">Total</th>
      </tr>
    </thead>
    <tbody>
      {#each historyData as order (order.idPurchase)}
        <tr>
          <td class="border px-4 py-2">{order.collectTime}</td>
          <td class="border px-4 py-2">{order.restaurant.companyName}</td>
          <td class="border px-4 py-2">{order.total}</td>
        </tr>
      {/each}
    </tbody>
  </table>
</div>
</div>
