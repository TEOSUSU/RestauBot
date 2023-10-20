<script>
  import Navbar from './Navbar.svelte';
  let open = false;
</script>

<Navbar/>
<!-- svelte-ignore a11y-no-static-element-interactions -->
<div class="p-4 sm:ml-64"  
  on:click={() => open = false}
  on:keydown={(e) => {
    if (e.key === 'Escape') open = false;
  }}>
<h1 class="text-3xl">Welcome to SvelteKit</h1>
<p class="text-3xl ">Visit <a href="https://kit.svelte.dev">kit.svelte.dev</a> to read the documentation</p>
<button class="bg-green-500 hover:bg-green-600 text-white  font-bold py-2 px-4 rounded">
    Button
  </button>

  <button class="bg-transparent hover:bg-blue-500 text-blue-700 font-semibold hover:text-white py-2 px-4 border border-blue-500 hover:border-transparent rounded">
    Button
  </button>
  <button class="bg-gray-300 hover:bg-gray-400 text-gray-800 font-bold py-2 px-4 rounded inline-flex items-center">
    <svg class="fill-current w-4 h-4 mr-2" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"><path d="M13 8V2H7v6H2l8 8 8-8h-5zM0 18h20v2H0v-2z"/></svg>
    <span>Download</span>
  </button>

</div>