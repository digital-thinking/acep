<template>
    <div>
        <h3>Item</h3>
        <!--        <AssetSymbolSearch v-if="showSearch"></AssetSymbolSearch> -->
        <td>
            <router-link :to="{name: 'PortfolioView', params: {portfolioId: portfolio.id}}">{{ portfolio.id }}
            </router-link>
        </td>
        <td>{{ portfolio.name }}</td>
        <td>{{ portfolio.created ? $d(Date.parse(portfolio.created), 'short') : '' }}</td>
        <td class="text-right">
            <div class="btn-group">
                <router-link :to="{name: 'PortfolioView', params: {portfolioId: portfolio.id}}"
                             class="btn btn-info btn-sm details"
                             data-cy="entityDetailsButton" tag="button">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                </router-link>
            </div>
        </td>
    </div>
</template>

<script>
import AssetSymbolSearch from "@/entities/symbol/asset-symbol";

export default {
    name: "PortfolioView",
    components: {AssetSymbolSearch},
    props: ['portfolio'],
    data() {
        return {
            showSearch: true
        }
    }
}
</script>

<style scoped>

</style>
