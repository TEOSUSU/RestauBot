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
    let openRow;
    let details;
    // let countsMenu = {};
    // let countsPurchase = {};
    var countDish = {};
    var countMenu = {};
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
      console.log("PURCHASE DETAILS")
      console.log(responseData)
      purchaseDetailData = responseData; // Mettez à jour purchaseDetailData avec les données de l'API
      
	    purchaseDetailData.forEach(function (a) {
	      count[a.idDish] = (count[a.idPurchase] || 0) + 1;
	    });
      console.log("COUNT DISH");
      console.log(countDish);
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
      console.log("MENU DETAILS");
      console.log(menuDetailData);

      menuDetailData.forEach(function (a) {
        count[a.idMenu] = (count[a.idMenu] || 0) + 1;
      });
      console.log("COUNT MENU");
      console.log(countMenu);
    })
    .catch(error => {
      console.error("Erreur lors de la récupération des détails de la commande menu :", error);
      
    });
    // let countsMenu = countOccurrences(menuDetailData);
    // let countsPurchase = countOccurrences(purchaseDetailData);
    
}



// function countOccurrences(data) {
//     let counts = {};

//     for (let dish of data) {
//       // Convert the order object to a string to use as a key
//       let key = JSON.stringify(dish);

//       // If this order is already in the counts object, increment their count
//       // Otherwise, set their count to 1
//       counts[key] = counts[key] ? counts[key] + 1 : 1;
//     }
//     console.log("COUNTS");
//     console.log(counts);
//     return counts;
//   }

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
                    <!-- {#each menuDetailData as  menu (menu.idMenu)} -->
                    {#each menuDetailData as  menu, index (index)}
                    <!-- {#each Object.entries(countsMenu) as [menu, count] (menu)} -->
                      <div class="flex justify-between overflow-hidden mb-4">
                        <div class="px-4 py-3 sm:px-6">
                          <h3 class=" leading-6 font-medium text-gray-900">
                            <!-- {#if count. > 1}
                              {count} x 
                            {/if} -->
                            {countMenu[menu.name]} x 
                            <!-- {JSON.parse(menu).name} -->
                            {menu.name}
                          </h3>
                        </div>
                        <div class="px-4 py-3 sm:px-6">
                          <p class="mt-1 max-w-2xl text-sm text-gray-500">
                            {menu.price} €
                            <!-- {JSON.parse(menu).price} € -->
                          </p>
                        </div>
                      </div>
                    {/each}
                    {#each purchaseDetailData as dish, index (index)}
                    <!-- {#each Object.entries(countsPurchase) as [dish, count] (dish)} -->
                      <div class="flex justify-between overflow-hidden mb-4">
                        <div class="px-4 py-3 sm:px-6">
                          <h3 class=" leading-6 font-medium text-gray-900">
                            <!-- {#if count > 1}
                              {count} x 
                            {/if} -->
                            {countDish[dish.name]} x 
                            {dish.name}
                            <!-- {JSON.parse(dish).name} -->
                          </h3>
                        </div>
                        <div class="px-4 py-3 sm:px-6">
                          <p class="mt-1 max-w-2xl text-sm text-gray-500">
                            {dish.price} €
                            <!-- {JSON.parse(dish).price} -->
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


</div>
</div>
