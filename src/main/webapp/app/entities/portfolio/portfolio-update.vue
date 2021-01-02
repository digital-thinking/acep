<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <form name="editForm" role="form" novalidate v-on:submit.prevent="save()" >
                <h2 id="acepApp.portfolio.home.createOrEditLabel" data-cy="PortfolioCreateUpdateHeading" v-text="$t('acepApp.portfolio.home.createOrEditLabel')">Create or edit a Portfolio</h2>
                <div>
                    <div class="form-group" v-if="portfolio.id">
                        <label for="id" v-text="$t('global.field.id')">ID</label>
                        <input type="text" class="form-control" id="id" name="id"
                               v-model="portfolio.id" readonly />
                    </div>
                    <div class="form-group">
                        <label class="form-control-label" v-text="$t('acepApp.portfolio.name')" for="portfolio-name">Name</label>
                        <input type="text" class="form-control" name="name" id="portfolio-name" data-cy="name"
                            :class="{'valid': !$v.portfolio.name.$invalid, 'invalid': $v.portfolio.name.$invalid }" v-model="$v.portfolio.name.$model"  required/>
                        <div v-if="$v.portfolio.name.$anyDirty && $v.portfolio.name.$invalid">
                            <small class="form-text text-danger" v-if="!$v.portfolio.name.required" v-text="$t('entity.validation.required')">
                                This field is required.
                            </small>
                            <small class="form-text text-danger" v-if="!$v.portfolio.name.minLength"
                                   v-text="$t('entity.validation.minlength', {min: 1})">
                                This field is required to be at least 1 characters.
                            </small>
                            <small class="form-text text-danger" v-if="!$v.portfolio.name.maxLength"
                                   v-text="$t('entity.validation.maxlength', {max: 255})">
                                This field cannot be longer than 255 characters.
                            </small>
                        </div>
                    </div>
                    <div v-if="portfolio.created" class="form-group">
                        <label class="form-control-label" for="portfolio-created"
                               v-text="$t('acepApp.portfolio.createdDate')">Created</label>
                        <div class="d-flex">
                            <input id="portfolio-created" data-cy="created" type="datetime-local" class="form-control"
                                   name="created"
                                   :class="{'valid': !$v.portfolio.created.$invalid, 'invalid': $v.portfolio.created.$invalid }"

                                   :value="convertDateTimeFromServer($v.portfolio.created.$model)"
                                   @change="updateInstantField('created', $event)"/>
                        </div>
                    </div>
                </div>
                <div>
                    <button type="button" id="cancel-save" class="btn btn-secondary" v-on:click="previousState()">
                        <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
                    </button>
                    <button type="submit" id="save-entity" data-cy="entityCreateSaveButton" :disabled="$v.portfolio.$invalid || isSaving" class="btn btn-primary">
                        <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>
<script lang="ts" src="./portfolio-update.component.ts">
</script>
