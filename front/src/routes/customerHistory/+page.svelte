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
    const menuDetailApiUrl = 'http://localhost:8080/api/menus/details/'
    let menuDetailApiUrlFinal = '';
    let historyData = [];
    let purchaseDetailData = [];
    let menuDetailData = [];
    let openRow
    let details
    const test = 1;
    const toggleRow = (order) => {
      openRow = openRow === order ? null : order;
      if (openRow === order) {
        handleOrderClick(order.idPurchase);
      }
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
     historyData = responseData;
     console.log(historyData); // Mettez à jour historyData avec les données de l'API
    })
    .catch(error => {
      console.error("Erreur lors de la récupération des détails de l'historique :", error);
      
    });
    
});




function handleOrderClick(orderId) {
  purchaseDetailFinalUrl = purchaseDetailApiUrl+orderId;
  menuDetailApiUrlFinal = menuDetailApiUrl+orderId;
  fetch(purchaseDetailFinalUrl, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
    .then(response => response.json())
    .then(responseData => {
      // console.log("PURCHASE DETAILS")
      // console.log(responseData)
      purchaseDetailData = responseData; // Mettez à jour purchaseDetailData avec les données de l'API
    })
    .catch(error => {
      console.error("Erreur lors de la récupération des détails de la commande dish :", error);
      
    });

    fetch(menuDetailApiUrlFinal, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
    })
      .then(response => response.json())
      .then(responseData => {
      menuDetailData = responseData; // Mettez à jour purchaseDetailData avec les données de l'API
    })
    .catch(error => {
      console.error("Erreur lors de la récupération des détails de la commande menu :", error);
      
    });
}

</script>
<Navbar/>
<div class="p-4 sm:ml-64 flex flex-col items-center">
  <h1 class="font-bold text-xl pb-5 text-gray-900">Historique des commandes</h1>
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
          <TableBodyRow on:click={() => toggleRow(order)} class="hover:bg-slate-100">
            <TableBodyCell class="text-gray-900">{order.collectTime}</TableBodyCell>
            <TableBodyCell class="text-gray-900">{order.restaurant.companyName}</TableBodyCell>
            <TableBodyCell class="text-gray-500">{order.total} €</TableBodyCell>
          </TableBodyRow>
          {#if openRow === order}
            
            <TableBodyRow on:dblclick= {console.log("doubleclick")}>

              <TableBodyCell colspan="4" class="p-0">
                <div class="px-2 py-3" transition:slide={{ duration: 300, axis: 'y' }}>
                  <div class="container mx-auto">
                    {#each menuDetailData as  menu (menu.idMenu)}
                      <div class="flex justify-between overflow-hidden mb-4">
                        <div class="px-4 py-3 sm:px-6">
                          <h3 class=" leading-6 font-medium text-gray-900">
                            {menu.name}
                          </h3>
                        </div>
                        <div class="px-4 py-3 sm:px-6">
                          <p class="mt-1 max-w-2xl text-sm text-gray-500">
                            {menu.price} €
                          </p>
                        </div>
                      </div>
                    {/each}
                    {#each purchaseDetailData as dish (dish.idDish)}
                      <div class="flex justify-between overflow-hidden mb-4">
                        <div class="px-4 py-3 sm:px-6">
                          <h3 class=" leading-6 font-medium text-gray-900">
                            {dish.name}
                          </h3>
                        </div>
                        <div class="px-4 py-3 sm:px-6">
                          <p class="mt-1 max-w-2xl text-sm text-gray-500">
                            {dish.price} €
                          </p>
                        </div>
                      </div>
                    {/each}
                  </div>
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
