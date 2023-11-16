<script>
    import { page } from '$app/stores';
    import { onMount } from "svelte";
    import { writable } from 'svelte/store';

    const url = $page.url;
    const customerId = parseInt(url.searchParams.get('customer'));
    const customerApiUrl = `http://localhost:8080/api/purchases/customer/${customerId}`;
    let historyData = [[]];
onMount(() => {
  fetch(customerApiUrl, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
    .then(response => response.json())
    .then(responseData => {
     historyData = responseData; // Mettez à jour historyData avec les données de l'API
    })
    .catch(error => {
      console.error("Erreur lors de la récupération des détails de l'historique :", error);
      
    });
});



</script>
<div class="p-4 sm:ml-64">
<table class="table-auto">
  <thead>
    <tr>
      <th class="px-4 py-2">Date</th>
      <th class="px-4 py-2">Restaurant</th>
      <th class="px-4 py-2">Total</th>
    </tr>
  </thead>
  <tbody>
    {#each historyData as order (order.idPurchase)}
      <tr>
        <td class="border px-4 py-2">{order.collectTime}</td>
        <!-- <td class="border px-4 py-2">{order.restaurant.companyName}</td> -->
        <td class="border px-4 py-2">{order.total}</td>
      </tr>
    {/each}
  </tbody>
</table>
</div>
