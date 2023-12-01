<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11">
    import Swal from 'sweetalert2';
    let headersList = {
        "Accept": "*/*"
    }

    export let data;
	import Navbar from '../../Navbar.svelte';
	import { invalidateAll } from '$app/navigation';
    
	let categories = data.allCategories;
    let types = data.allTypes;
    let formSubmitted = false;
    let product = data.productSelected
	let name = product.name;
	let description = product.description;
	let price = product.price;
    let photoFile = product.photoFile;
    let newCategoryName;
    let showAddCategoryInput = false;

    async function addCategory() {
        if (newCategoryName){
            const maxIdCategory = categories.reduce((max, category) => {
                return category.idCategory > max ? category.idCategory : max;
                }, 0);
            categories = [...categories, { idCategory: maxIdCategory + 1,
                name: newCategoryName }];
            const body = {
                name: newCategoryName
            }
            const response = await fetch('http://localhost:8080/api/categories/create/1', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(body)
            });
            if(response.ok){
                newCategoryName = '';
                showAddCategoryInput = false;
                invalidateAll();
                Swal.fire({
                    title: 'Bien joué !',
                    text: 'Catégorie ajouté avec succès !',
                    icon: 'success',
                    confirmButtonText: 'Fermer',
                    confirmButtonColor: 'green'
                });
            }else{
                Swal.fire({
                    title: 'Oops...',
                    text: 'Une erreur est survenue',
                    icon: 'warning',
                    confirmButtonText: 'Fermer',
                    confirmButtonColor: 'green',
                });
            }
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
            const response = await fetch('http://localhost:8080/api/types/create/1', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(body)
            });
            
            if (response.ok) {
                newTypeName = '';
                showAddTypeInput = false;
                invalidateAll();
                Swal.fire({
                    title: 'Bien joué !',
                    text: 'Type ajouté avec succès !',
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
	}

    let selectedType = product.type.idType;
	let selectedCategorie = product.type.category.idCategory;

	async function modifyDish() {
        let formData = new FormData();
        formData.append('file', photoFile[0]);
        formData.append('name', name);
        formData.append('description', description);
        formData.append('price', price);
        formData.append('typeId',  selectedType);
        formData.append('restaurantId', 1);

        const response = await fetch(`http://localhost:8080/api/dishes/modify/${product.idDish}`, {
			method: 'POST',
			body: formData,
            headers: headersList
		});
        if (response.ok) {
            formSubmitted = true;
            name = "";
            description = "";
            price = "";
            photoFile = "";
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
                text: "Votre plat a été supprimé.",
                icon: "success"
                });
                const response = await fetch(`http://localhost:8080/api/dishes/delete/${product.idDish}`, {
                    method: 'POST',
                    headers: headersList
                });
                if (response.ok) {
                    invalidateAll();
                    Swal.fire({
                        title: 'Bien joué !',
                        text: 'Plat supprimé avec succès !',
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

    function handleCategoryChange() {
        selectedType = ''; 
    }
</script>

<Navbar/>
<main class="centered">
        <div>Modifiez le plat : {product.name}</div>
        <form on:submit|preventDefault={modifyDish} enctype="multipart/form-data">

            <h2>Catégorie</h2>

            <select bind:value={selectedCategorie} on:change={handleCategoryChange}
            class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 " 
            required>
                <option value="" disabled >Sélectionner une catégorie</option>
                {#each categories as categorie}
                    <option value={categorie.idCategory}>
                        {categorie.name}
                    </option>
                {/each}
            </select>
            
            {#if !showAddCategoryInput}
            <button on:click={() => showAddCategoryInput = true}
                class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center ">
                Ajouter
            </button>
             {/if}
        
        {#if showAddCategoryInput}
        <div>
            <input type="text" bind:value={newCategoryName} 
            class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 " 
            placeholder="Nouvelle catégorie" 
        />
            <button on:click={addCategory}
            class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center ">
                Ajouter
            </button>
            <button on:click={() => showAddCategoryInput = false}
                class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center ">
                Annuler
            </button>
        </div>
        {/if}

            <h2>Type</h2>

            {#if selectedCategorie}
                <select bind:value={selectedType}
                class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5" 
                required>
                    <option value="" disabled >Sélectionner un type</option>
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
                        class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center ">
                        Ajouter
                    </button>
                {/if}
                
                {#if showAddTypeInput}
                <div>
                    <input 
                        type="text" 
                        bind:value={newTypeName} 
                        class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 " 
                        placeholder="Nouveau type" 
                    />
                    <button on:click={addType}
                    class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center ">
                        Ajouter
                    </button>
                    <button on:click={() => showAddTypeInput = false}
                        class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center ">
                        Annuler
                    </button>
                </div>
                {/if}
            {:else}
                <p>Veuillez choisir une catégorie</p>
            {/if}

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

        <button  on:click={deleteMenu}
                class="text-white bg-green-700 hover:bg-green-800 focus:ring-4 focus:outline-none focus:ring-green-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 mb-5 mt-5 text-center ">
                Supprimer le plat
        </button>
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