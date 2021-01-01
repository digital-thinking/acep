<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <div v-if="portfolioEntry">
                <h2 class="jh-entity-heading" data-cy="portfolioEntryDetailsHeading"><span v-text="$t('acepApp.portfolioEntry.detail.title')">PortfolioEntry</span> {{portfolioEntry.id}}</h2>
                <dl class="row jh-entity-details">
                    <dt>
                        <span v-text="$t('acepApp.portfolioEntry.amount')">Amount</span>
                    </dt>
                    <dd>
                        <span>{{portfolioEntry.amount}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('acepApp.portfolioEntry.price')">Price</span>
                    </dt>
                    <dd>
                        <span>{{portfolioEntry.price}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('acepApp.portfolioEntry.bought')">Bought</span>
                    </dt>
                    <dd>
                        <span v-if="portfolioEntry.bought">{{$d(Date.parse(portfolioEntry.bought), 'long') }}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('acepApp.portfolioEntry.sold')">Sold</span>
                    </dt>
                    <dd>
                        <span v-if="portfolioEntry.sold">{{$d(Date.parse(portfolioEntry.sold), 'long') }}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('acepApp.portfolioEntry.customName')">Custom Name</span>
                    </dt>
                    <dd>
                        <span>{{portfolioEntry.customName}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('acepApp.portfolioEntry.group1')">Group 1</span>
                    </dt>
                    <dd>
                        <span>{{portfolioEntry.group1}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('acepApp.portfolioEntry.group2')">Group 2</span>
                    </dt>
                    <dd>
                        <span>{{portfolioEntry.group2}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('acepApp.portfolioEntry.group3')">Group 3</span>
                    </dt>
                    <dd>
                        <span>{{portfolioEntry.group3}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('acepApp.portfolioEntry.group4')">Group 4</span>
                    </dt>
                    <dd>
                        <span>{{portfolioEntry.group4}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('acepApp.portfolioEntry.portfolio')">Portfolio</span>
                    </dt>
                    <dd>
                        <div v-if="portfolioEntry.portfolio">
                            <router-link :to="{name: 'PortfolioView', params: {portfolioId: portfolioEntry.portfolio.id}}">{{portfolioEntry.portfolio.id}}</router-link>
                        </div>
                    </dd>
                    <dt>
                        <span v-text="$t('acepApp.portfolioEntry.asset')">Asset</span>
                    </dt>
                    <dd>
                        <div v-if="portfolioEntry.asset">
                            <router-link :to="{name: 'AssetView', params: {assetId: portfolioEntry.asset.id}}">{{portfolioEntry.asset.id}}</router-link>
                        </div>
                    </dd>
                </dl>
                <button type="submit"
                        v-on:click.prevent="previousState()"
                        class="btn btn-info"
                        data-cy="entityDetailsBackButton">
                    <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
                </button>
                <router-link v-if="portfolioEntry.id" :to="{name: 'PortfolioEntryEdit', params: {portfolioEntryId: portfolioEntry.id}}" tag="button" class="btn btn-primary">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
                </router-link>
            </div>
        </div>
    </div>
</template>

<script lang="ts" src="./portfolio-entry-details.component.ts">
</script>
