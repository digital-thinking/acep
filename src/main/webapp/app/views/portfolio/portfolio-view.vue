<template>
    <div>
        <!--        <AssetSymbolSearch v-if="showSearch"></AssetSymbolSearch> -->
        <!--        <td>-->
        <!--            <router-link :to="{name: 'PortfolioView', params: {portfolioId: portfolio.id}}">{{ portfolio.id }}-->
        <!--            </router-link>-->
        <!--        </td>-->

        <h3>{{ dto.portfolio.name }}</h3>
        <div>{{ dto.portfolio.created ? $d(Date.parse(dto.portfolio.created), 'short') : '' }}</div>
        <!--        <div>{{ dto.portfolioNumbers.performanceAmount }} {{ dto.portfolioNumbers.currency }}</div>-->
        <!--        <h4>{{ dto.portfolioNumbers.performancePercent * 100 }} %</h4>-->
        <td class="text-right">
            <div class="btn-group">
                <router-link :to="{name: 'PortfolioView', params: {portfolioId: dto.portfolio.id}}"
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
import {PortfolioDTO} from "@/shared/dtos/portfolios-dto.model";

export default {
    name: "PortfolioView",
    components: {AssetSymbolSearch},
    props: {
        dto: PortfolioDTO,
    },
    data() {
        return {
            showSearch: true
        }
    }
}
</script>

<style scoped>

</style>
