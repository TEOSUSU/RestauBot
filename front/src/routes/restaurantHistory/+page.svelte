<script>
	import { page } from '$app/stores';
	import { onMount } from 'svelte';
	import { writable } from 'svelte/store';
	import Cookies from 'js-cookie';
	import Navbar from '../Navbar.svelte';
	import { sessionStorage } from '../../stores/stores.js';
	import { goto } from '$app/navigation';
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
	export let data;
	let userInfo = data.userInfo;
	const restaurantId = userInfo.idUser;
	const restaurantApiUrl = `http://localhost:8080/api/purchases/restaurant/${restaurantId}`;
	const purchaseDetailApiUrl = 'http://localhost:8080/api/dishes/details/';
	let purchaseDetailFinalUrl = '';
	const menuDetailApiUrl = 'http://localhost:8080/api/menus/details/';
	let menuDetailApiUrlFinal = '';
	let historyData = [];
	let purchaseDetailData = [];
	let menuDetailData = [];
	let openRow;
	let details;
	const headersList = {
		'Content-Type': 'application/json',
		Authorization: 'Bearer ' + Cookies.get('token')
	};
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
	};

	onMount(() => {
		fetch(restaurantApiUrl, {
			method: 'GET',
			headers: headersList
		})
			.then((response) => response.json())
			.then((responseData) => {
				historyData = responseData;
			})
			.catch((error) => {
				console.error("Erreur lors de la récupération des détails de l'historique :", error);
			});
		if (!userInfo || !userInfo.role) {
			// Stocker l'URL actuelle dans le store de session
			sessionStorage.redirectUrl = window.location.pathname;
			// Rediriger vers la page de connexion
			goto('/auth');
		}
		if (userInfo.role === 'ROLE_CUSTOMER') {
			goto(`http://localhost:5173/clientModification/${userInfo.idUser}`);
		}
	});

	function handleOrderClick(orderId) {
		purchaseDetailFinalUrl = purchaseDetailApiUrl + orderId;
		menuDetailApiUrlFinal = menuDetailApiUrl + orderId;
		fetch(purchaseDetailFinalUrl, {
			method: 'GET',
			headers: headersList
		})
			.then((response) => response.json())
			.then((responseData) => {
				purchaseDetailData = responseData; 
			})
			.catch((error) => {
				console.error('Erreur lors de la récupération des détails de la commande dish :', error);
			});

		fetch(menuDetailApiUrlFinal, {
			method: 'GET',
			headers: headersList
		})
			.then((response) => response.json())
			.then((responseData) => {
				menuDetailData = responseData; 
			})
			.catch((error) => {
				console.error('Erreur lors de la récupération des détails de la commande menu :', error);
			});
	}
</script>

<Navbar {userInfo} />
{#if userInfo.role === 'ROLE_RESTAURANT'}
	<div class="p-4 sm:ml-64 flex flex-col items-center">
		<h1 class="font-bold text-xl pb-5 text-gray-900">Historique des commandes</h1>
		<div class="overflow-x-auto shadow-md rounded-lg">
			<Table>
				<TableHead class="dark:bg-slate-300 bg-slate-300">
					<TableHeadCell>Date</TableHeadCell>
					<TableHeadCell>Client</TableHeadCell>
					<TableHeadCell>Total</TableHeadCell>
				</TableHead>
				<TableBody class="divide-y">
					{#each historyData as order (order.idPurchase)}
						<TableBodyRow on:click={() => toggleRow(order)} class="hover:bg-slate-100">
							<TableBodyCell class="text-gray-900">{order.collectTime}</TableBodyCell>
							<TableBodyCell class="text-gray-900"
								>{order.customer.firstname} {order.customer.surname}</TableBodyCell
							>
							<TableBodyCell class="text-gray-500">{order.total} €</TableBodyCell>
						</TableBodyRow>
						{#if openRow === order}
							<TableBodyRow>
								<TableBodyCell colspan="4" class="p-0">
									<div class="px-2 py-3" transition:slide={{ duration: 300, axis: 'y' }}>
										<div class="container mx-auto">
											{#each menuDetailData as menu, index (index)}
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
											{#each purchaseDetailData as dish, index (index)}
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
		</div>
	</div>
{:else}
	<div>Vous n'avez pas accès à cette page!</div>
{/if}
