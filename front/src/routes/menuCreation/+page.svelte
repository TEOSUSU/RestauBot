<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11">
    import Swal from 'sweetalert2';
    import { Button, Dropdown, DropdownItem, Checkbox } from 'flowbite-svelte';
    import { ChevronDownSolid } from 'flowbite-svelte-icons';
	import { invalidateAll } from '$app/navigation';
	let name;
	let description;
	let price;
    let photoFile;
    let headersList = {
        "Accept": "*/*"
    }

    export let data;
	import Navbar from '../Navbar.svelte';
    let dishes = data.allDishes;
	let categories = data.allCategories;
    let types = data.allTypes;
    let selectedDishes = [];
    let selectedCategories = [];

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

    async function createMenu() {
        let formData = new FormData();
        formData.append('file', photoFile[0]);
        formData.append('name', name);
        formData.append('description', description);
        formData.append('price', price);
        formData.append('dishes',  selectedDishes);
        formData.append('restaurantId', 1);

        const response = await fetch('http://localhost:8080/api/menus/create', {
			method: 'POST',
			body: formData,
            headers: headersList
		});
        if (response.ok) {
            name = "";
            description = "";
            price = "";
            photoFile = "";
            selectedDishes = "";
            selectedCategories = "";
            invalidateAll();
            Swal.fire({
                title: 'Bien joué !',
                text: 'Menu ajouté avec succès !',
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
</script>

<Navbar/>
<main class="centered">
    <div>Ajoutez un nouveau menu</div>
    
        <form on:submit|preventDefault={createMenu} enctype="multipart/form-data">
                
            <input 
                bind:value={name}
                type="text" 
                id="name" 
                class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 " 
                placeholder="Name" 
                required
            />

            <input 
                bind:value={description}
                type="text" 
                id="description" 
                class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5" 
                placeholder="Description" 
                required
            />

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

            <p class="w-full">Sélectionnez les plats que vous souhaitez inclure au menu :</p>

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

            <input 
                bind:value={price}
                type="number" 
                id="price" 
                class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5" 
                placeholder="Prix €" 
                step="0.01"
                required
            />

           <input 
                bind:files={photoFile} 
                type="file" 
                id="photoFile" 
                accept="image/*" 
                required
                class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"/>

            <button type="submit"
                class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center ">
                Submit
            </button>
        </form>
        
</main>


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