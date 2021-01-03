<template>
    <div>
        <h2 id="page-heading" data-cy="AssetHeading">
            <span id="asset-heading" v-text="$t('acepApp.symbol.search')">Symbols</span>
        </h2>
        <div class="row">
            <div class="col-sm-12">
                <form class="form-inline" name="searchForm" v-on:submit.prevent="search(currentSearch)">
                    <div class="input-group w-100 mt-3">
                        <input id="currentSearch" v-model="currentSearch" class="form-control" name="currentSearch"
                               type="text"
                               v-bind:placeholder="$t('acepApp.asset.home.search')"/>
                        <button id="launch-search" class="btn btn-primary" type="button"
                                v-on:click="search(currentSearch)">
                            <font-awesome-icon icon="search"></font-awesome-icon>
                        </button>
                    </div>
                </form>
            </div>
        </div>
        <br/>
        <div v-if="!isFetching && symbols && symbols.length === 0" class="alert alert-warning">
            <span v-text="$t('acepApp.asset.home.notFound')">No assets found</span>
        </div>
        <div v-if="symbols && symbols.length > 0" class="table-responsive">
            <table aria-describedby="assets" class="table table-striped">
                <thead>
                <tr>
                    <th scope="row"><span v-text="$t('acepApp.asset.name')">Name</span></th>
                    <th scope="row"><span v-text="$t('acepApp.asset.symbol')">Symbol</span></th>
                    <th scope="row"><span v-text="$t('acepApp.asset.currency')">Currency</span></th>
                    <th scope="row"><span v-text="$t('acepApp.asset.type')">Asset Type</span></th>
                    <th scope="row"><span v-text="$t('acepApp.asset.region')">Region</span></th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="assetSymbol in symbols"
                    :key="assetSymbol.id" data-cy="entityTable">
                    <td>
                        <router-link :to="{name: 'AssetAddBySymbol', params: {assetId: assetSymbol.id}}">
                            {{ assetSymbol.id }}
                        </router-link>
                        {{ assetSymbol.name }}
                    </td>
                    <td>{{ assetSymbol.symbol }}</td>
                    <td>{{ assetSymbol.currency }}</td>
                    <td>{{ assetSymbol.assetType }}</td>
                    <td>{{ assetSymbol.region }}</td>
                </tr>
                </tbody>
            </table>
        </div>
        <b-modal id="addAsset" ref="addAsset">
            <span slot="modal-title"><span id="acepApp.asset.delete.question" data-cy="assetDeleteDialogHeading"
                                           v-text="$t('entity.delete.title')">Confirm delete operation</span></span>
            <div class="modal-body">
                <p id="jhi-delete-asset-heading" v-text="$t('acepApp.asset.delete.question', {'id': addId})">Are you
                    sure you want to add this Asset?</p>
            </div>
            <div slot="modal-footer">
                <button class="btn btn-secondary" type="button" v-on:click="closeDialog()"
                        v-text="$t('entity.action.cancel')">Cancel
                </button>
                <button id="jhi-confirm-delete-asset" class="btn btn-primary" data-cy="entityConfirmDeleteButton"
                        type="button" v-on:click="addAset()" v-text="$t('entity.action.delete')">Add
                </button>
            </div>
        </b-modal>
    </div>
</template>

<script lang="ts" src="./asset-symbol.component.ts">
</script>
