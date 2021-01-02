<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <div v-if="portfolio">
                <h2 class="jh-entity-heading" data-cy="portfolioDetailsHeading"><span v-text="$t('acepApp.portfolio.detail.title')">Portfolio</span> {{portfolio.id}}</h2>
                <dl class="row jh-entity-details">
                    <dt>
                        <span v-text="$t('acepApp.portfolio.name')">Name</span>
                    </dt>
                    <dd>
                        <span>{{portfolio.name}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('acepApp.portfolio.createdDate')">Created</span>
                    </dt>
                    <dd>
                        <span v-if="portfolio.created">{{$d(Date.parse(portfolio.created), 'long') }}</span>
                    </dd>
                </dl>
                <button type="submit"
                        v-on:click.prevent="previousState()"
                        class="btn btn-info"
                        data-cy="entityDetailsBackButton">
                    <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
                </button>
                <router-link v-if="portfolio.id" :to="{name: 'PortfolioEdit', params: {portfolioId: portfolio.id}}" tag="button" class="btn btn-primary">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
                </router-link>
            </div>
        </div>
    </div>
</template>

<script lang="ts" src="./portfolio-details.component.ts">
</script>
