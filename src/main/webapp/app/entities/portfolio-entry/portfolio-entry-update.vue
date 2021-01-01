<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <form name="editForm" role="form" novalidate v-on:submit.prevent="save()" >
                <h2 id="acepApp.portfolioEntry.home.createOrEditLabel" data-cy="PortfolioEntryCreateUpdateHeading" v-text="$t('acepApp.portfolioEntry.home.createOrEditLabel')">Create or edit a PortfolioEntry</h2>
                <div>
                    <div class="form-group" v-if="portfolioEntry.id">
                        <label for="id" v-text="$t('global.field.id')">ID</label>
                        <input type="text" class="form-control" id="id" name="id"
                               v-model="portfolioEntry.id" readonly />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('acepApp.portfolioEntry.amount')" for="portfolio-entry-amount">Amount</label>
                        <input type="number" class="form-control" name="amount" id="portfolio-entry-amount" data-cy="amount"
                            :class="{'valid': !$v.portfolioEntry.amount.$invalid, 'invalid': $v.portfolioEntry.amount.$invalid }" v-model.number="$v.portfolioEntry.amount.$model"  required/>
                        <div v-if="$v.portfolioEntry.amount.$anyDirty && $v.portfolioEntry.amount.$invalid">
                            <small class="form-text text-danger" v-if="!$v.portfolioEntry.amount.required" v-text="$t('entity.validation.required')">
                                This field is required.
                            </small>
                            <small class="form-text text-danger" v-if="!$v.portfolioEntry.amount.numeric" v-text="$t('entity.validation.number')">
                                This field should be a number.
                            </small>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('acepApp.portfolioEntry.price')" for="portfolio-entry-price">Price</label>
                        <input type="number" class="form-control" name="price" id="portfolio-entry-price" data-cy="price"
                            :class="{'valid': !$v.portfolioEntry.price.$invalid, 'invalid': $v.portfolioEntry.price.$invalid }" v-model.number="$v.portfolioEntry.price.$model"  required/>
                        <div v-if="$v.portfolioEntry.price.$anyDirty && $v.portfolioEntry.price.$invalid">
                            <small class="form-text text-danger" v-if="!$v.portfolioEntry.price.required" v-text="$t('entity.validation.required')">
                                This field is required.
                            </small>
                            <small class="form-text text-danger" v-if="!$v.portfolioEntry.price.numeric" v-text="$t('entity.validation.number')">
                                This field should be a number.
                            </small>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('acepApp.portfolioEntry.bought')" for="portfolio-entry-bought">Bought</label>
                        <div class="d-flex">
                            <input id="portfolio-entry-bought" data-cy="bought" type="datetime-local" class="form-control" name="bought" :class="{'valid': !$v.portfolioEntry.bought.$invalid, 'invalid': $v.portfolioEntry.bought.$invalid }"
                             required
                            :value="convertDateTimeFromServer($v.portfolioEntry.bought.$model)"
                            @change="updateInstantField('bought', $event)"/>
                        </div>
                        <div v-if="$v.portfolioEntry.bought.$anyDirty && $v.portfolioEntry.bought.$invalid">
                            <small class="form-text text-danger" v-if="!$v.portfolioEntry.bought.required" v-text="$t('entity.validation.required')">
                                This field is required.
                            </small>
                            <small class="form-text text-danger" v-if="!$v.portfolioEntry.bought.ZonedDateTimelocal" v-text="$t('entity.validation.ZonedDateTimelocal')">
                                This field should be a date and time.
                            </small>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('acepApp.portfolioEntry.sold')" for="portfolio-entry-sold">Sold</label>
                        <div class="d-flex">
                            <input id="portfolio-entry-sold" data-cy="sold" type="datetime-local" class="form-control" name="sold" :class="{'valid': !$v.portfolioEntry.sold.$invalid, 'invalid': $v.portfolioEntry.sold.$invalid }"
                            
                            :value="convertDateTimeFromServer($v.portfolioEntry.sold.$model)"
                            @change="updateInstantField('sold', $event)"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('acepApp.portfolioEntry.customName')" for="portfolio-entry-customName">Custom Name</label>
                        <input type="text" class="form-control" name="customName" id="portfolio-entry-customName" data-cy="customName"
                            :class="{'valid': !$v.portfolioEntry.customName.$invalid, 'invalid': $v.portfolioEntry.customName.$invalid }" v-model="$v.portfolioEntry.customName.$model" />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('acepApp.portfolioEntry.group1')" for="portfolio-entry-group1">Group 1</label>
                        <input type="text" class="form-control" name="group1" id="portfolio-entry-group1" data-cy="group1"
                            :class="{'valid': !$v.portfolioEntry.group1.$invalid, 'invalid': $v.portfolioEntry.group1.$invalid }" v-model="$v.portfolioEntry.group1.$model" />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('acepApp.portfolioEntry.group2')" for="portfolio-entry-group2">Group 2</label>
                        <input type="text" class="form-control" name="group2" id="portfolio-entry-group2" data-cy="group2"
                            :class="{'valid': !$v.portfolioEntry.group2.$invalid, 'invalid': $v.portfolioEntry.group2.$invalid }" v-model="$v.portfolioEntry.group2.$model" />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('acepApp.portfolioEntry.group3')" for="portfolio-entry-group3">Group 3</label>
                        <input type="text" class="form-control" name="group3" id="portfolio-entry-group3" data-cy="group3"
                            :class="{'valid': !$v.portfolioEntry.group3.$invalid, 'invalid': $v.portfolioEntry.group3.$invalid }" v-model="$v.portfolioEntry.group3.$model" />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('acepApp.portfolioEntry.group4')" for="portfolio-entry-group4">Group 4</label>
                        <input type="text" class="form-control" name="group4" id="portfolio-entry-group4" data-cy="group4"
                            :class="{'valid': !$v.portfolioEntry.group4.$invalid, 'invalid': $v.portfolioEntry.group4.$invalid }" v-model="$v.portfolioEntry.group4.$model" />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('acepApp.portfolioEntry.portfolio')" for="portfolio-entry-portfolio">Portfolio</label>
                        <select class="form-control" id="portfolio-entry-portfolio" data-cy="portfolio" name="portfolio" v-model="portfolioEntry.portfolio">
                            <option v-bind:value="null"></option>
                            <option v-bind:value="portfolioEntry.portfolio && portfolioOption.id === portfolioEntry.portfolio.id ? portfolioEntry.portfolio : portfolioOption" v-for="portfolioOption in portfolios" :key="portfolioOption.id">{{portfolioOption.id}}</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('acepApp.portfolioEntry.asset')" for="portfolio-entry-asset">Asset</label>
                        <select class="form-control" id="portfolio-entry-asset" data-cy="asset" name="asset" v-model="portfolioEntry.asset">
                            <option v-bind:value="null"></option>
                            <option v-bind:value="portfolioEntry.asset && assetOption.id === portfolioEntry.asset.id ? portfolioEntry.asset : assetOption" v-for="assetOption in assets" :key="assetOption.id">{{assetOption.id}}</option>
                        </select>
                    </div>
                </div>
                <div>
                    <button type="button" id="cancel-save" class="btn btn-secondary" v-on:click="previousState()">
                        <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
                    </button>
                    <button type="submit" id="save-entity" data-cy="entityCreateSaveButton" :disabled="$v.portfolioEntry.$invalid || isSaving" class="btn btn-primary">
                        <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>
<script lang="ts" src="./portfolio-entry-update.component.ts">
</script>
