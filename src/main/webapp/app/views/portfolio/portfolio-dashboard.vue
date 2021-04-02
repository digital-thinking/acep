<template>
    <div>
        <h1>Portfolios</h1>
        <ul>
            <li
                is="PortfolioView"
                v-for="(dto, index) in portfoliosDTOs"
                v-bind:key="dto.id"
                v-bind:index="index"
                v-bind:dto="dto"
                v-on:remove="portfoliosDTOs.splice(index, 1)"
            ></li>
        </ul>
    </div>
</template>

<script lang="ts" src="./portfolio-dashboard-component.ts">
</script>
