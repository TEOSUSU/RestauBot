<script>
    import "../app.css";
	  import { SvelteToast } from '@zerodevx/svelte-toast'
  </script>
  
  <div class="container">	
    <SvelteToast />
    <slot />
  </div>
  
  <style>
    .container {
      background-color: #ffffff;
      min-height: 100vh;
      min-width: 100vw;
      position: absolute;
      top: 0;
      left: 0;
      font-family: "Lato", 'sans-serif';
    }
  </style>
  <slot />