<template>
    <div>
        <h2 id="page-heading" data-cy="AssetHeading">
            <span v-text="$t('acepApp.asset.home.title')" id="asset-heading">Assets</span>
            <div class="d-flex justify-content-end">
                <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
                    <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span v-text="$t('userManagement.home.refreshListLabel')">Refresh List</span>
                </button>
                <router-link :to="{name: 'AssetCreate'}" tag="button" id="jh-create-entity" data-cy="entityCreateButton" class="btn btn-primary jh-create-entity create-asset">
                    <font-awesome-icon icon="plus"></font-awesome-icon>
                    <span  v-text="$t('acepApp.asset.home.createLabel')">
                        Create a new Asset
                    </span>
                </router-link>
            </div>
        </h2>
        <div class="row">
            <div class="col-sm-12">
                <form name="searchForm" class="form-inline" v-on:submit.prevent="search(currentSearch)">
                    <div class="input-group w-100 mt-3">
                        <input type="text" class="form-control" name="currentSearch" id="currentSearch"
                            v-bind:placeholder="$t('acepApp.asset.home.search')"
                            v-model="currentSearch" />
                        <button type="button" id="launch-search" class="btn btn-primary" v-on:click="search(currentSearch)">
                            <font-awesome-icon icon="search"></font-awesome-icon>
                        </button>
                        <button type="button" id="clear-search" class="btn btn-secondary" v-on:click="clear()"
                            v-if="currentSearch">
                            <font-awesome-icon icon="trash"></font-awesome-icon>
                        </button>
                    </div>
                </form>
            </div>
        </div>
        <br/>
        <div class="alert alert-warning" v-if="!isFetching && assets && assets.length === 0">
            <span v-text="$t('acepApp.asset.home.notFound')">No assets found</span>
        </div>
        <div class="table-responsive" v-if="assets && assets.length > 0">
            <table class="table table-striped" aria-describedby="assets">
                <thead>
                <tr>
                    <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
                    <th scope="row"><span v-text="$t('acepApp.asset.name')">Name</span></th>
                    <th scope="row"><span v-text="$t('acepApp.asset.currency')">Currency</span></th>
                    <th scope="row"><span v-text="$t('acepApp.asset.assetType')">Asset Type</span></th>
                    <th scope="row"><span v-text="$t('acepApp.asset.symbol')">Symbol</span></th>
                    <th scope="row"><span v-text="$t('acepApp.asset.source')">Source</span></th>
                    <th scope="row"></th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="asset in assets"
                    :key="asset.id" data-cy="entityTable">
                    <td>
                        <router-link :to="{name: 'AssetView', params: {assetId: asset.id}}">{{asset.id}}</router-link>
                    </td>
                    <td>{{asset.name}}</td>
                    <td v-text="$t('acepApp.Currency.' + asset.currency)">{{asset.currency}}</td>
                    <td v-text="$t('acepApp.AssetType.' + asset.assetType)">{{asset.assetType}}</td>
                    <td>{{asset.symbol}}</td>
                    <td v-text="$t('acepApp.Source.' + asset.source)">{{asset.source}}</td>
                    <td class="text-right">
                        <div class="btn-group">
                            <router-link :to="{name: 'AssetView', params: {assetId: asset.id}}" tag="button" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                                <font-awesome-icon icon="eye"></font-awesome-icon>
                                <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                            </router-link>
                            <router-link :to="{name: 'AssetEdit', params: {assetId: asset.id}}" tag="button" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                                <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                                <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                            </router-link>
                            <b-button v-on:click="prepareRemove(asset)"
                                   variant="danger"
                                   class="btn btn-sm"
                                   data-cy="entityDeleteButton"
                                   v-b-modal.removeEntity>
                                <font-awesome-icon icon="times"></font-awesome-icon>
                                <span class="d-none d-md-inline" v-text="$t('entity.action.delete')">Delete</span>
                            </b-button>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <b-modal ref="removeEntity" id="removeEntity" >
            <span slot="modal-title"><span id="acepApp.asset.delete.question" data-cy="assetDeleteDialogHeading" v-text="$t('entity.delete.title')">Confirm delete operation</span></span>
            <div class="modal-body">
                <p id="jhi-delete-asset-heading" v-text="$t('acepApp.asset.delete.question', {'id': removeId})">Are you sure you want to delete this Asset?</p>
            </div>
            <div slot="modal-footer">
                <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
                <button type="button" class="btn btn-primary" id="jhi-confirm-delete-asset" data-cy="entityConfirmDeleteButton" v-text="$t('entity.action.delete')" v-on:click="removeAsset()">Delete</button>
            </div>
        </b-modal>
    </div>
</template>

<script lang="ts" src="./asset.component.ts">
</script>
