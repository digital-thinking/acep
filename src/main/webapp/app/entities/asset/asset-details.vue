<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <div v-if="asset">
                <h2 class="jh-entity-heading" data-cy="assetDetailsHeading"><span v-text="$t('acepApp.asset.detail.title')">Asset</span> {{asset.id}}</h2>
                <dl class="row jh-entity-details">
                    <dt>
                        <span v-text="$t('acepApp.asset.name')">Name</span>
                    </dt>
                    <dd>
                        <span>{{asset.name}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('acepApp.asset.currency')">Currency</span>
                    </dt>
                    <dd>
                        <span v-text="$t('acepApp.Currency.' + asset.currency)">{{asset.currency}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('acepApp.asset.assetType')">Asset Type</span>
                    </dt>
                    <dd>
                        <span v-text="$t('acepApp.AssetType.' + asset.assetType)">{{asset.assetType}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('acepApp.asset.symbol')">Symbol</span>
                    </dt>
                    <dd>
                        <span>{{asset.symbol}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('acepApp.asset.source')">Source</span>
                    </dt>
                    <dd>
                        <span v-text="$t('acepApp.Source.' + asset.source)">{{asset.source}}</span>
                    </dd>
                </dl>
                <button type="submit"
                        v-on:click.prevent="previousState()"
                        class="btn btn-info"
                        data-cy="entityDetailsBackButton">
                    <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
                </button>
                <router-link v-if="asset.id" :to="{name: 'AssetEdit', params: {assetId: asset.id}}" tag="button" class="btn btn-primary">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
                </router-link>
            </div>
        </div>
    </div>
</template>

<script lang="ts" src="./asset-details.component.ts">
</script>
