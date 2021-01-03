<template>
    <div>
        <h1>Dashboard</h1>
        <ul>
            <li
                is="PortfolioView"
                v-for="(portfolio, index) in portfolios"
                v-bind:key="portfolio.id"
                v-bind:index="index"
                v-bind:portfolio="portfolio"
                v-on:remove="portfolio.splice(index, 1)"
            ></li>
        </ul>
    </div>
</template>

<script lang="ts" src="./portfolio-dashboard-component.ts">
</script>
