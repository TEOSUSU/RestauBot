<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11">
    import Swal from 'sweetalert2';
    import { Button, Dropdown, DropdownItem, Checkbox } from 'flowbite-svelte';
    import { ChevronDownSolid } from 'flowbite-svelte-icons';
	import { invalidateAll } from '$app/navigation';
    const headersList = {
      'Content-Type': 'application/json',
      Authorization: 'Bearer ' + Cookies.get('token')
    };
    import { onMount } from 'svelte';

    export let data;
	import Cookies from 'js-cookie';
	import Navbar from '../Navbar.svelte';
    let dishes = data.allDishes;
	let categories = data.allCategories;
    let types = data.allTypes;
    let menu = data.menuSelected;
	let name = menu.name;
	let userInfo = data.userInfo;
	let description = menu.description;
	let price = menu.price;
    let photoFile = menu.photoFile;
    let selectedDishes = menu.assignedDishes.map(dish => dish.idDish);
    let selectedCategories = Array.from(new Set
    (menu.assignedDishes.map(dish => dish.type.category.idCategory)));

    onMount(() => {
    if (!import.meta.env.SSR) {
      // Récupérer les données actuelles du panier depuis le stockage de session
      cartData = $sessionStorage || [];
    }
    if (!userInfo || !userInfo.role) {
      // Stocker l'URL actuelle dans le store de session
      sessionStorage.redirectUrl = window.location.pathname;
      // Rediriger vers la page de connexion
      goto('/auth');
    }
	});

    function handleCheckboxChangeDish(dishId) {
        if (selectedDishes.includes(dishId)) {
            selectedDishes = selectedDishes.filter(id => id !== dishId);
        } else {
            selectedDishes = [...selectedDishes, dishId];
        }
    }

    function handleCheckboxChangeCategory(categoryId) {
        if (selectedCategories.includes(categoryId)) {
            selectedCategories = selectedCategories.filter(id => id !== categoryId);
            selectedDishes = selectedDishes.filter(dishId => {
            const dish = dishes.find(d => d.idDish === dishId);
            return dish && dish.type.category.idCategory !== categoryId;
        });
        } else {
            selectedCategories = [...selectedCategories, categoryId];
        }
    }

    function isDishSelected(dishId) {
        return selectedDishes.includes(dishId);
    }

    function isCategorySelected(categoryId) {
        return selectedCategories.includes(categoryId);
    }

    async function modifyMenu() {
        let formData = new FormData();
        formData.append('file', photoFile[0]);
        formData.append('name', name);
        formData.append('description', description);
        formData.append('price', price);
        formData.append('dishes',  selectedDishes);
        formData.append('restaurantId', 1);

        const response = await fetch(`http://localhost:8080/api/menus/modify/${menu.idMenu}`, {
			method: 'POST',
			body: formData,
            headers: headersList,
		});
        if (response.ok) {
            invalidateAll();
            Swal.fire({
                title: 'Bien joué !',
                text: 'Menu modifié avec succès !',
                icon: 'success',
                confirmButtonText: 'Fermer',
                confirmButtonColor: 'green'
            });
        }
        else{
            Swal.fire({
                title: 'Oops...',
                text: 'Une erreur est survenue',
                icon: 'warning',
                confirmButtonText: 'Fermer',
                confirmButtonColor: 'green',
            });
        }
	}

    async function deleteMenu() {
        Swal.fire({
            title: "Etes-vous sûr?",
            text: "Vous ne pourrez pas annuler!",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Oui, supprimer!"
            }).then(async (result) => {
            if (result.isConfirmed) {
                Swal.fire({
                title: "Supprimé!",
                text: "Votre menu a été supprimé.",
                icon: "success"
                });
                const response = await fetch(`http://localhost:8080/api/menus/delete/${menu.idMenu}`, {
                    method: 'POST',
                    headers: headersList
                });
                if (response.ok) {
                    invalidateAll();
                    Swal.fire({
                        title: 'Bien joué !',
                        text: 'Menu supprimé avec succès !',
                        icon: 'success',
                        confirmButtonText: 'Fermer',
                        confirmButtonColor: 'green'
                    });
                    import('$app/navigation').then(({ goto }) => {
                            goto('/RestaurantMenu');
                        });
                }
                else{
                    Swal.fire({
                        title: 'Oops...',
                        text: 'Une erreur est survenue',
                        icon: 'warning',
                        confirmButtonText: 'Fermer',
                        confirmButtonColor: 'green',
                    });
                }
            }
            })
        
	}
</script>

<Navbar {userInfo} />
{#if userInfo.role === 'ROLE_RESTAURANT'}
    <main class="centered">
        <div>Modifier le menu : {menu.name}</div>
        
            <form on:submit|preventDefault={modifyMenu} enctype="multipart/form-data">
                <div>
                    <p>Nom</p>
                    <input 
                        bind:value={name}
                        type="text" 
                        id="name" 
                        class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 " 
                        placeholder="Name" 
                        required
                    />
                </div>

                <div>
                    <p>Description</p>
                    <input 
                        bind:value={description}
                        type="text" 
                        id="description" 
                        class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5" 
                        placeholder="Description" 
                        required
                    />
                </div>

                <p class="w-full">Sélectionnez les catégories que vous souhaitez inclure au menu :</p>
                
                {#each categories as categorie}
                    <label>
                        <input
                            type="checkbox"
                            on:change={() => handleCheckboxChangeCategory(categorie.idCategory)}
                            checked={isCategorySelected(categorie.idCategory)}
                        />
                        {categorie.name}
                    </label>
                {/each}

                {#if selectedCategories.length != 0}
                    <p class="w-full">Sélectionnez les plats que vous souhaitez inclure au menu :</p>
                {/if}

                {#each types.filter(d => selectedCategories.includes(d.category.idCategory)) as type}
                    <Button class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center ">
                        {type.name}<ChevronDownSolid class="w-3 h-3 ml-2 text-black dark:text-blue" /></Button>
                    <Dropdown class="w-44 p-3 space-y-3 text-sm">
                        {#each dishes.filter(d => d.type.idType === type.idType) as dish}
                            <li>
                                <Checkbox class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 "
                                    on:change={() => handleCheckboxChangeDish(dish.idDish)}
                                    checked={isDishSelected(dish.idDish)}>
                                    {dish.name}
                                </Checkbox>
                            </li>
                        {/each}
                    </Dropdown>
                {/each}

                <div>
                    <p>Prix</p>
                    <input 
                        bind:value={price}
                        type="number" 
                        id="price" 
                        class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5" 
                        placeholder="Prix €" 
                        step="0.01"
                        required
                    />
                </div>

                <div>
                    <p>Photo</p>
                <input 
                        bind:files={photoFile} 
                        type="file" 
                        id="photoFile" 
                        accept="image/*" 
                        required
                        class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"/>
                </div>

                <button type="submit"
                    class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 mb-2 text-center ">
                    Valider la modification
                </button>
            </form>
                
            <button  on:click={deleteMenu}
                    class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 mb-5 mt-5 text-center ">
                    Supprimer le menu
            </button>
            
    </main>
{:else}
<div>
  Vous n'avez pas accès à cette page!
</div>
{/if}

<style>
	.centered {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		height: 100vh;
	}

	form {
		display: flex;
		flex-direction: column;
		gap: 1rem;
        width: 20%;
	}
    .form-description {
        margin-bottom: 0;
        white-space: pre-line;
    }
</style>