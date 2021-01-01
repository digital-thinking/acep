<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <form name="editForm" role="form" novalidate v-on:submit.prevent="save()" >
                <h2 id="acepApp.asset.home.createOrEditLabel" data-cy="AssetCreateUpdateHeading" v-text="$t('acepApp.asset.home.createOrEditLabel')">Create or edit a Asset</h2>
                <div>
                    <div class="form-group" v-if="asset.id">
                        <label for="id" v-text="$t('global.field.id')">ID</label>
                        <input type="text" class="form-control" id="id" name="id"
                               v-model="asset.id" readonly />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('acepApp.asset.name')" for="asset-name">Name</label>
                        <input type="text" class="form-control" name="name" id="asset-name" data-cy="name"
                            :class="{'valid': !$v.asset.name.$invalid, 'invalid': $v.asset.name.$invalid }" v-model="$v.asset.name.$model"  required/>
                        <div v-if="$v.asset.name.$anyDirty && $v.asset.name.$invalid">
                            <small class="form-text text-danger" v-if="!$v.asset.name.required" v-text="$t('entity.validation.required')">
                                This field is required.
                            </small>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('acepApp.asset.currency')" for="asset-currency">Currency</label>
                        <select class="form-control" name="currency" :class="{'valid': !$v.asset.currency.$invalid, 'invalid': $v.asset.currency.$invalid }" v-model="$v.asset.currency.$model" id="asset-currency" data-cy="currency"  required>
                            <option value="AED" v-bind:label="$t('acepApp.Currency.AED')">AED</option>
                            <option value="USD" v-bind:label="$t('acepApp.Currency.USD')">USD</option>
                        </select>
                        <div v-if="$v.asset.currency.$anyDirty && $v.asset.currency.$invalid">
                            <small class="form-text text-danger" v-if="!$v.asset.currency.required" v-text="$t('entity.validation.required')">
                                This field is required.
                            </small>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('acepApp.asset.assetType')" for="asset-assetType">Asset Type</label>
                        <select class="form-control" name="assetType" :class="{'valid': !$v.asset.assetType.$invalid, 'invalid': $v.asset.assetType.$invalid }" v-model="$v.asset.assetType.$model" id="asset-assetType" data-cy="assetType"  required>
                            <option value="Stock" v-bind:label="$t('acepApp.AssetType.Stock')">Stock</option>
                            <option value="Forex" v-bind:label="$t('acepApp.AssetType.Forex')">Forex</option>
                            <option value="Crypto" v-bind:label="$t('acepApp.AssetType.Crypto')">Crypto</option>
                            <option value="Custom" v-bind:label="$t('acepApp.AssetType.Custom')">Custom</option>
                        </select>
                        <div v-if="$v.asset.assetType.$anyDirty && $v.asset.assetType.$invalid">
                            <small class="form-text text-danger" v-if="!$v.asset.assetType.required" v-text="$t('entity.validation.required')">
                                This field is required.
                            </small>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('acepApp.asset.symbol')" for="asset-symbol">Symbol</label>
                        <input type="text" class="form-control" name="symbol" id="asset-symbol" data-cy="symbol"
                            :class="{'valid': !$v.asset.symbol.$invalid, 'invalid': $v.asset.symbol.$invalid }" v-model="$v.asset.symbol.$model"  required/>
                        <div v-if="$v.asset.symbol.$anyDirty && $v.asset.symbol.$invalid">
                            <small class="form-text text-danger" v-if="!$v.asset.symbol.required" v-text="$t('entity.validation.required')">
                                This field is required.
                            </small>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('acepApp.asset.source')" for="asset-source">Source</label>
                        <select class="form-control" name="source" :class="{'valid': !$v.asset.source.$invalid, 'invalid': $v.asset.source.$invalid }" v-model="$v.asset.source.$model" id="asset-source" data-cy="source"  required>
                            <option value="AlphaVantage" v-bind:label="$t('acepApp.Source.AlphaVantage')">AlphaVantage</option>
                            <option value="Binance" v-bind:label="$t('acepApp.Source.Binance')">Binance</option>
                        </select>
                        <div v-if="$v.asset.source.$anyDirty && $v.asset.source.$invalid">
                            <small class="form-text text-danger" v-if="!$v.asset.source.required" v-text="$t('entity.validation.required')">
                                This field is required.
                            </small>
                        </div>
                    </div>
                </div>
                <div>
                    <button type="button" id="cancel-save" class="btn btn-secondary" v-on:click="previousState()">
                        <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
                    </button>
                    <button type="submit" id="save-entity" data-cy="entityCreateSaveButton" :disabled="$v.asset.$invalid || isSaving" class="btn btn-primary">
                        <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>
<script lang="ts" src="./asset-update.component.ts">
</script>
