import App from './App.svelte';
import { createSSRApp } from 'vue';
import { Router } from 'svelte-routing';

const app = createSSRApp(App);

app.use(Router);

export { app };
