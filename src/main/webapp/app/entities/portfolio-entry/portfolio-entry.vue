<template>
    <div>
        <h2 id="page-heading" data-cy="PortfolioEntryHeading">
            <span v-text="$t('acepApp.portfolioEntry.home.title')" id="portfolio-entry-heading">Portfolio Entries</span>
            <div class="d-flex justify-content-end">
                <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
                    <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span v-text="$t('userManagement.home.refreshListLabel')">Refresh List</span>
                </button>
                <router-link :to="{name: 'PortfolioEntryCreate'}" tag="button" id="jh-create-entity" data-cy="entityCreateButton" class="btn btn-primary jh-create-entity create-portfolio-entry">
                    <font-awesome-icon icon="plus"></font-awesome-icon>
                    <span  v-text="$t('acepApp.portfolioEntry.home.createLabel')">
                        Create a new Portfolio Entry
                    </span>
                </router-link>
            </div>
        </h2>
        <div class="row">
            <div class="col-sm-12">
                <form name="searchForm" class="form-inline" v-on:submit.prevent="search(currentSearch)">
                    <div class="input-group w-100 mt-3">
                        <input type="text" class="form-control" name="currentSearch" id="currentSearch"
                            v-bind:placeholder="$t('acepApp.portfolioEntry.home.search')"
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
        <div class="alert alert-warning" v-if="!isFetching && portfolioEntries && portfolioEntries.length === 0">
            <span v-text="$t('acepApp.portfolioEntry.home.notFound')">No portfolioEntries found</span>
        </div>
        <div class="table-responsive" v-if="portfolioEntries && portfolioEntries.length > 0">
            <table class="table table-striped" aria-describedby="portfolioEntries">
                <thead>
                <tr>
                    <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
                    <th scope="row"><span v-text="$t('acepApp.portfolioEntry.amount')">Amount</span></th>
                    <th scope="row"><span v-text="$t('acepApp.portfolioEntry.price')">Price</span></th>
                    <th scope="row"><span v-text="$t('acepApp.portfolioEntry.bought')">Bought</span></th>
                    <th scope="row"><span v-text="$t('acepApp.portfolioEntry.sold')">Sold</span></th>
                    <th scope="row"><span v-text="$t('acepApp.portfolioEntry.customName')">Custom Name</span></th>
                    <th scope="row"><span v-text="$t('acepApp.portfolioEntry.group1')">Group 1</span></th>
                    <th scope="row"><span v-text="$t('acepApp.portfolioEntry.group2')">Group 2</span></th>
                    <th scope="row"><span v-text="$t('acepApp.portfolioEntry.group3')">Group 3</span></th>
                    <th scope="row"><span v-text="$t('acepApp.portfolioEntry.group4')">Group 4</span></th>
                    <th scope="row"><span v-text="$t('acepApp.portfolioEntry.portfolio')">Portfolio</span></th>
                    <th scope="row"><span v-text="$t('acepApp.portfolioEntry.asset')">Asset</span></th>
                    <th scope="row"></th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="portfolioEntry in portfolioEntries"
                    :key="portfolioEntry.id" data-cy="entityTable">
                    <td>
                        <router-link :to="{name: 'PortfolioEntryView', params: {portfolioEntryId: portfolioEntry.id}}">{{portfolioEntry.id}}</router-link>
                    </td>
                    <td>{{portfolioEntry.amount}}</td>
                    <td>{{portfolioEntry.price}}</td>
                    <td>{{portfolioEntry.bought ? $d(Date.parse(portfolioEntry.bought), 'short') : ''}}</td>
                    <td>{{portfolioEntry.sold ? $d(Date.parse(portfolioEntry.sold), 'short') : ''}}</td>
                    <td>{{portfolioEntry.customName}}</td>
                    <td>{{portfolioEntry.group1}}</td>
                    <td>{{portfolioEntry.group2}}</td>
                    <td>{{portfolioEntry.group3}}</td>
                    <td>{{portfolioEntry.group4}}</td>
                    <td>
                        <div v-if="portfolioEntry.portfolio">
                            <router-link :to="{name: 'PortfolioView', params: {portfolioId: portfolioEntry.portfolio.id}}">{{portfolioEntry.portfolio.id}}</router-link>
                        </div>
                    </td>
                    <td>
                        <div v-if="portfolioEntry.asset">
                            <router-link :to="{name: 'AssetView', params: {assetId: portfolioEntry.asset.id}}">{{portfolioEntry.asset.id}}</router-link>
                        </div>
                    </td>
                    <td class="text-right">
                        <div class="btn-group">
                            <router-link :to="{name: 'PortfolioEntryView', params: {portfolioEntryId: portfolioEntry.id}}" tag="button" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                                <font-awesome-icon icon="eye"></font-awesome-icon>
                                <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                            </router-link>
                            <router-link :to="{name: 'PortfolioEntryEdit', params: {portfolioEntryId: portfolioEntry.id}}" tag="button" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                                <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                                <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                            </router-link>
                            <b-button v-on:click="prepareRemove(portfolioEntry)"
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
            <span slot="modal-title"><span id="acepApp.portfolioEntry.delete.question" data-cy="portfolioEntryDeleteDialogHeading" v-text="$t('entity.delete.title')">Confirm delete operation</span></span>
            <div class="modal-body">
                <p id="jhi-delete-portfolioEntry-heading" v-text="$t('acepApp.portfolioEntry.delete.question', {'id': removeId})">Are you sure you want to delete this Portfolio Entry?</p>
            </div>
            <div slot="modal-footer">
                <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
                <button type="button" class="btn btn-primary" id="jhi-confirm-delete-portfolioEntry" data-cy="entityConfirmDeleteButton" v-text="$t('entity.action.delete')" v-on:click="removePortfolioEntry()">Delete</button>
            </div>
        </b-modal>
    </div>
</template>

<script lang="ts" src="./portfolio-entry.component.ts">
</script>
