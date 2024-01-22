<!-- App.svelte -->
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11">
	import Swal from 'sweetalert2';
	import { onMount } from 'svelte';
	import { sessionStorage } from '../../../stores/stores.js';
	import { goto } from '$app/navigation';
	import Returnbar from '../../Returnbar.svelte';
	import Cookies from 'js-cookie';
	import Navbar from '../../Navbar.svelte';

	const headersList = {
		'Content-Type': 'application/json',
		Authorization: 'Bearer ' + Cookies.get('token')
	};

	let cartData = [];
	export let data;
	let categories = data.allCategories;
	let categoriesSelected = {};
	let addMenu;
	let existingMenus;
	let cardDataMenu;
	let userInfo = data.userInfo;
	let menuId = data.idMenu;

	let menu = {
		id: 2,
		name: 'Nom du produit',
		description: 'Description du produit',
		image: '../src/images/pizza.jpeg',
		quantity: 1,
		price: 10.99,
		idRestaurant: 0
	};

	if (!import.meta.env.SSR) {
		onMount(() => {
			cartData = $sessionStorage || [];

			const menuApiUrl = `http://localhost:8080/api/menus/${menuId}`;
			fetch(menuApiUrl, {
				method: 'GET',
				headers: headersList
			})
				.then((response) => response.json())
				.then((responseData) => {
					console.log(responseData);
					menu = responseData;
					menu.quantity = 1;
					menu.idRestaurant = responseData.restaurant.idUser;
					categoriesSelected = {};
					categories.forEach((category) => {
						// Initialisation conditionnelle
						categoriesSelected[category.idCategory] = !(
							menu.assignedDishes &&
							menu.assignedDishes.some(
								(dish) =>
									dish.type &&
									dish.type.category &&
									dish.type.category.idCategory === category.idCategory
							)
						);
					});
				})
				.catch((error) => {
					console.error('Erreur lors de la récupération des détails du produit :', error);
				});
			if (!userInfo || !userInfo.role) {
				// Stocker l'URL actuelle dans le store de session
				sessionStorage.redirectUrl = window.location.pathname + '?menu=' + menu.id;
				// Rediriger vers la page de connexion
				console.log(sessionStorage.redirectUrl);
				goto('/auth');
			}
				console.log(cartData)
				console.log(menu)
		});
	}

	function handleAddToCart() {
		if (!Object.values(categoriesSelected).every((value) => value === true)) {
			Swal.fire({
				icon: 'warning',
				title: 'Sélectionnez au moins un plat par catégorie',
				showConfirmButton: true
			});
		} else {
			addToCart(
				menu.name,
				menu.description,
				menu.price,
				menu.quantity,
				menu.idRestaurant,
				selectedDishes
			);
		}
	}

	function addToCart(name, description, price, quantity, idRestaurant, selectedDishes) {
		if (cartData.length == 0 || cartData[0].idRestaurant == menu.idRestaurant || cartData[0].idUser == menu.idRestaurant) {
			addMenu = true;
			console.log(cartData);
			cardDataMenu = cartData.filter((item) => item.idRestaurant)
			existingMenus = cardDataMenu.filter((item) => item.id.startsWith('menu'));
			if (existingMenus.length != 0) {
				for (var i = 0; i < existingMenus.length; i++) {
					if (dishesAreEqual(existingMenus[i].selectedDishes, selectedDishes)) {
						existingMenus[i].quantity += quantity;
						addMenu = false;
					}
				}
			}
			if (addMenu) {
				let nextId;

				if (existingMenus.length > 0) {
					const maxId = Math.max(...existingMenus.map((item) => parseInt(item.id.slice(4))));
					nextId = maxId + 1;
				} else {
					nextId = 0;
				}
				cartData = [
					...cartData,
					{
						id: 'menu' + nextId,
						name,
						description,
						price,
						quantity,
						idRestaurant,
						selectedDishes: { ...selectedDishes }
					}
				];
			}

			console.log(cartData);

			$sessionStorage = cartData;
			selectedDishes = {};

			let timerInterval;
			Swal.fire({
				title: 'Produit ajouté !',
				text: 'Votre article à bien été ajouté au panier !',
				icon: 'success',
				showConfirmButton: false,
				timer: 3000,
				timerProgressBar: true,
				willClose: () => {
					clearInterval(timerInterval);
				}
			}).then((result) => {
				/* Read more about handling dismissals below */
				if (result.dismiss === Swal.DismissReason.timer) {
					console.log('I was closed by the timer');
				}
			});
		} else {
			Swal.fire({
				icon: 'error',
				title: 'Aie !',
				text: "Vous ne pouvez pas mettre des articles de différent restaurants dans votre panier ! \n Voulez-vous vider le panier actuel afin d'ajouter quand même ce produit ?",
				showCancelButton: true,
				confirmButtonColor: '#22c55e',
				confirmButtonText: 'Vider le panier',
				cancelButtonText: 'Annuler'
			}).then((result) => {
				/* Read more about isConfirmed, isDenied below */
				if (result.isConfirmed) {
					clearCart();
					addToCart(
						menu.name,
						menu.description,
						menu.price,
						menu.quantity,
						menu.idRestaurant,
						selectedDishes
					);
				}
			});
		}
	}

	function dishesAreEqual(dishes1, dishes2) {
		if (Object.keys(dishes1).length !== Object.keys(dishes2).length) {
			return false;
		}

		for (const id in dishes1) {
			if (!dishes2[id] || !areDishesEqual(dishes1[id], dishes2[id])) {
				return false;
			}
		}

		return true;
	}

	function areDishesEqual(dish1, dish2) {
		return (
			dish1.idDish === dish2.idDish &&
			dish1.name === dish2.name &&
			dish1.description === dish2.description &&
			dish1.price === dish2.price
		);
	}

	function increaseQuantity() {
		menu.quantity += 1;
	}

	function decreaseQuantity() {
		if (menu.quantity > 1) {
			menu.quantity -= 1;
		}
	}

	function clearCart() {
		cartData = [];
		$sessionStorage = cartData;
	}

	function handleKeyDown(event, dish) {
		if (event.key === 'Enter' || event.key === ' ') {
			selectedDish = dish;
		}
	}

	let selectedDishes = {};

	function toggleDishSelection(category, dish) {
		if (!selectedDishes[category.idCategory]) {
			categoriesSelected[category.idCategory] = true;
			selectedDishes[category.idCategory] = dish;
		} else if (selectedDishes[category.idCategory] === dish) {
			categoriesSelected[category.idCategory] = false;
			selectedDishes[category.idCategory] = null;
		} else {
			categoriesSelected[category.idCategory] = true;
			selectedDishes[category.idCategory] = dish;
		}
	}
</script>

<Navbar {userInfo} />
<Returnbar {cartData} />

{#if userInfo.role === 'ROLE_RESTAURANT' || userInfo.role === 'ROLE_CUSTOMER'}
	<div class="p-4 sm:ml-64">
		<img src={menu.picture} alt={menu.name} class="w-full h-64 object-cover mb-4" />

		<h2 class="text-2xl font-bold mb-2">{menu.name}</h2>
		<p class="text-gray-600 mb-4">{menu.description}</p>
		<p class="text-gray-800 font-semibold mb-4">{menu.price} €</p>

		<main class="text-center overflow-hidden">
			{#each categories as category}
				{#if menu.assignedDishes && menu.assignedDishes.some((dish) => dish.type && dish.type.category && dish.type.category.idCategory === category.idCategory)}
					<div class="category m-4">
						<h2>{category.name}</h2>
						<div class="menu m-2">
							<div class="menu-items-container overflow-x-auto pb-4">
								<div class="menu-items flex whitespace-normal">
									{#if menu.assignedDishes}
										{#each menu.assignedDishes.filter((d) => d.type.category.idCategory == category.idCategory) as dish}
											<!-- svelte-ignore a11y-no-static-element-interactions -->
											<div
												on:click={() => toggleDishSelection(category, dish)}
												on:keydown={(e) => handleKeyDown(e, dish)}
												class:highlighted={selectedDishes[category.idCategory] === dish &&
													!!Object.keys(selectedDishes).length}
												class="menu-item border border-gray-300 p-4 text-left inline-block mr-4 whitespace-normal w-40 flex-shrink-0"
											>
												<img
													src={dish.picture}
													alt="{dish.name} Image"
													class="w-40 h-40 object-cover mb-2"
												/>
												<h3>{dish.name}</h3>
												<p class="description max-w-200 italic text-gray-500">
													{dish.type.name}<br />{dish.description}
												</p>
											</div>
										{/each}
									{/if}
								</div>
							</div>
						</div>
					</div>
				{/if}
			{/each}
		</main>
		{#if userInfo.role === 'ROLE_CUSTOMER'}
			<div class="flex items-center mb-4">
				<button
					on:click={decreaseQuantity}
					class="bg-gray-100 text-gray-700 px-4 py-2 rounded-l-full"
				>
					-
				</button>
				<span class="bg-gray-100 px-4 py-2">{menu.quantity}</span>
				<button
					on:click={increaseQuantity}
					class="bg-gray-100 text-gray-700 px-4 py-2 rounded-r-full"
				>
					+
				</button>
			</div>

			<button
				on:click={() => handleAddToCart()}
				class="w-full bg-green-500 text-white px-6 py-3 rounded"
			>
				Ajouter à la commande
			</button>
		{/if}
	</div>
{:else}
	<div>Vous n'avez pas accès à cette page!</div>
{/if}

<style>
	.highlighted {
		border-color: rgb(0, 0, 0);
	}
</style>
