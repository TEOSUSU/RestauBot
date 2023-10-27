<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11">
    import Swal from 'sweetalert2';
	let name;
	let description;
	let price;
    let photoFile;

    export let data;
	import Navbar from '../Navbar.svelte';
	import { invalidateAll } from '$app/navigation';
    
	let categories = data.allCategories;
    let types = data.allTypes;
    let formSubmitted = false;

    let newCategoryName;
    let showAddCategoryInput = false;

    async function addCategory() {
        if (newCategoryName){
            categories = [...categories, { name: newCategoryName }];
            const body = {
                name: newCategoryName
            }
            await fetch('http://localhost:8080/api/categories/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(body)
            });
            newCategoryName = '';
            showAddCategoryInput = false;
            console.log(categories);
            invalidateAll();
            Swal.fire({
                title: 'Bien joué !',
                text: 'Catégorie ajouté avec succès !',
                icon: 'success',
                confirmButtonText: 'Fermer',
                confirmButtonColor: 'green'
            });
         }
	}

    $:categories;

    let newTypeName;
    let showAddTypeInput = false;

    async function addType() {
        if (newTypeName){
            types = [...types, { name: newTypeName, category: {
                    idCategory: selectedCategorie
                }}];
            const body = {
                name: newTypeName,
                category: {
                    idCategory: selectedCategorie
                }
            }
            await fetch('http://localhost:8080/api/types/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(body)
            });
            newTypeName = '';
            showAddCategoryInput = false;
            invalidateAll();
            Swal.fire({
                title: 'Bien joué !',
                text: 'Type ajouté avec succès !',
                icon: 'success',
                confirmButtonText: 'Fermer',
                confirmButtonColor: 'green'
            });
        }
	}

	let selectedCategorie;
    let selectedType;

	async function createDish() {
        const formData = new FormData();
        formData.append('name', name);
        formData.append('description', description);
        formData.append('price', price);
        formData.append('type', {
                    idType: 2
                });
        formData.append('restaurant', {
                    idRestaurant: 2
                });
        formData.append('file', photoFile);

        await fetch('http://localhost:8080/api/dishes/create', {
			method: 'POST',
			body: formData
		});
        formSubmitted = true;
        name = "";
	    description = "";
	    price = "";
        photoFile = null;
        selectedCategorie = "";
        selectedType = "";
        invalidateAll();
        Swal.fire({
            title: 'Bien joué !',
            text: 'Plat ajouté avec succès !',
            icon: 'success',
            confirmButtonText: 'Fermer',
            confirmButtonColor: 'green'
        });
	}
</script>

<Navbar/>
<main class="centered">
        <form on:submit|preventDefault={createDish} enctype="multipart/form-data">

            <h2>Catégorie</h2>

            <select bind:value={selectedCategorie}
            class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" 
            required>
                <option value="" disabled selected>Sélectionner une catégorie</option>
                {#each categories as categorie}
                    <option value={categorie.idCategory}>
                        {categorie.name}
                    </option>
                {/each}
            </select>
            
            {#if !showAddCategoryInput}
            <button on:click={() => showAddCategoryInput = true}
                class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
                Ajouter
            </button>
             {/if}
        
        {#if showAddCategoryInput}
        <div>
            <input type="text" bind:value={newCategoryName} 
            class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" 
            placeholder="Nouvelle catégorie" 
        />
            <button on:click={addCategory}
            class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
                Ajouter
            </button>
            <button on:click={() => showAddCategoryInput = false}
                class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
                Annuler
            </button>
        </div>
        {/if}

            <h2>Type</h2>

            {#if selectedCategorie}
                <select bind:value={selectedType}
                class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" 
                required>
                    <option value="" disabled selected>Sélectionner un type</option>
                    {#each types as type}
                        {#if type.category.idCategory === selectedCategorie}
                            <option value={type.idType}>
                                {type.name}
                            </option>
                        {/if}
                    {/each}
                </select>

                {#if !showAddTypeInput}
                    <button on:click={() => showAddTypeInput = !showAddTypeInput}
                        class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
                        Ajouter
                    </button>
                {/if}
                
                {#if showAddTypeInput}
                <div>
                    <input 
                        type="text" 
                        bind:value={newTypeName} 
                        class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" 
                        placeholder="Nouveau type" 
                    />
                    <button on:click={addType}
                    class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
                        Ajouter
                    </button>
                    <button on:click={() => showAddTypeInput = false}
                        class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
                        Annuler
                    </button>
                </div>
                {/if}
            {:else}
                <p>Veuillez choisir une catégorie</p>
            {/if}

            
            <input 
                bind:value={name}
                type="text" 
                id="name" 
                class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" 
                placeholder="Name" 
                required
            />

            <input 
                bind:value={description}
                type="text" 
                id="description" 
                class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" 
                placeholder="Description" 
                required
            />

            <input 
                bind:value={price}
                type="number" 
                id="price" 
                class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" 
                placeholder="Prix €" 
                step="0.01"
                required
            />

           <input 
                type="file" 
                id="photo" 
                accept="image/*" 
                bind:files={photoFile} 
                required
                class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"/>

            <button type="submit"
                class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
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
	}
</style>