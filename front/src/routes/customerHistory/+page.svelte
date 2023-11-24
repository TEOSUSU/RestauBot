<script>
    import { page } from '$app/stores';
    import { onMount } from "svelte";
    import { writable } from 'svelte/store';
    import Navbar from '../Navbar.svelte';
    import {
    Table,
    TableBody,
    TableBodyCell,
    TableBodyRow,
    TableHead,
    TableHeadCell,
    ImagePlaceholder,
    Modal
    } from 'flowbite-svelte';
    import { slide } from 'svelte/transition';
    let open = false;
    const url = $page.url;
    const customerId = parseInt(url.searchParams.get('customer'));
    const customerApiUrl = `http://localhost:8080/api/purchases/customer/${customerId}`;
    const purchaseDetailApiUrl = 'http://localhost:8080/api/dishes/details/'
    let purchaseDetailFinalUrl = '';
    let historyData = [];
    let purchaseDetailData = [];
    let openRow
    let details
    const test = 1;
    const toggleRow = (order) => {
      openRow = openRow === order ? null : order
    }

onMount(() => {
  fetch(customerApiUrl, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
    .then(response => response.json())
    .then(responseData => {
      // console.log(responseData)
     historyData = responseData;
     console.log(historyData); // Mettez à jour historyData avec les données de l'API
    })
    .catch(error => {
      console.error("Erreur lors de la récupération des détails de l'historique :", error);
      
    });
    
});

// let selectedOrderId = 0;
function handleOrderClick(orderId) {
  console.log("ORDER ID");
  purchaseDetailFinalUrl = purchaseDetailApiUrl+orderId;
  console.log(purchaseDetailFinalUrl);
  fetch(purchaseDetailFinalUrl, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
    .then(response => response.json())
    .then(responseData => {
      console.log("PURCHASE DETAILS")
      console.log(responseData)
      purchaseDetailData = responseData; // Mettez à jour purchaseDetailData avec les données de l'API
    })
    .catch(error => {
      console.error("Erreur lors de la récupération des détails de la commande :", error);
      
    });
}

function logTest() {
  console.log("qsdfmlnfsqdoin");
  console.log(test);
}
</script>
<Navbar/>
<div class="p-4 sm:ml-64 flex flex-col items-center">
  <h1 class="font-bold text-xl pb-5">Historique des commandes</h1>
  <div class="overflow-x-auto shadow-md rounded-lg">
    <Table>
      <TableHead class="dark:bg-slate-300 bg-slate-300">
        <TableHeadCell>Date</TableHeadCell>
        <TableHeadCell>Restaurant</TableHeadCell>
        <TableHeadCell>Total</TableHeadCell>
      </TableHead>
      <TableBody class="divide-y">
        {#each historyData as order (order.idPurchase)}
        <!-- {#each items as item, i} -->
          <TableBodyRow on:click={() => toggleRow(order)}>
            <TableBodyCell>{order.collectTime}</TableBodyCell>
            <TableBodyCell>{order.restaurant.companyName}</TableBodyCell>
            <TableBodyCell>{order.idPurchase}</TableBodyCell>
          </TableBodyRow>
          {#if openRow === order}
          {handleOrderClick(order.idPurchase)}
          <!-- {#key order.idPurchase} -->
          <!-- {logTest()} -->
                <!-- <script>console.log("qsdfmlnfsqdoin");
                console.log(test);</script> -->
                <TableBodyRow on:dblclick= {console.log("doubleclick")}>

              <TableBodyCell colspan="4" class="p-0">
                <div class="px-2 py-3" transition:slide={{ duration: 300, axis: 'y' }}>
                  {#each purchaseDetailData as dish (dish.idDish)}
                    <li>{dish.name} {dish.price} €</li>
                  {/each}
                </div>
              </TableBodyCell>
            </TableBodyRow>
          {/if}
        {/each}
      </TableBody>
    </Table>
    <Modal title={details?.name} open={!!details} autoclose outsideclose>
      <ImagePlaceholder />
    </Modal>




  <!-- <table class="table-auto border-collapse ">
    <thead class="">
      <tr class="bg-slate-300 ">
        <th class="px-4 py-2">Date</th>
        <th class="px-4 py-2">Restaurant</th>
        <th class="px-4 py-2">Total</th>
      </tr>
    </thead>
    <tbody>
      {#each historyData as order (order.idPurchase)}
        <tr class="hover:bg-slate-100 duration-300 ease-in-out" on:click={() => handleOrderClick(order.total)}>
          <td class="border-b px-4 py-2">{order.collectTime}</td>
          <td class="border-b px-4 py-2">{order.restaurant.companyName}</td>
          <td class="border-b px-4 py-2">{order.total}</td>
        </tr>
      {/each}
    </tbody>
  </table> -->

</div>
</div>
