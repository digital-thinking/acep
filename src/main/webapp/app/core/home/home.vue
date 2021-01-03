<template>
    <div class="home row">
        <div class="col-md-9">
            <h1 v-if="username && authenticated" class="display-4"
                v-text="$t('home.logged.message', { username: username })">Hello "{{
                    username
                }}"</h1>
            <p class="lead" v-text="$t('home.subtitle')">Watch your portfolio</p>

            <PortfolioDashboard></PortfolioDashboard>
            <div>
                <div v-if="!authenticated" class="alert alert-warning">
                    <span v-text="$t('global.messages.info.authenticated.prefix')">If you want to </span>
                    <a class="alert-link" v-on:click="openLogin()"
                       v-text="$t('global.messages.info.authenticated.link')">sign
                        in</a
                    ><span v-html="$t('global.messages.info.authenticated.suffix')"
                >, you can try the default accounts:<br/>- Administrator (login="admin" and password="admin") <br/>- User (login="user" and
            password="user").</span
                >
                </div>
                <div v-if="!authenticated" class="alert alert-warning">
                    <span v-text="$t('global.messages.info.register.noaccount')">You don't have an account yet?</span>&nbsp;
                    <router-link class="alert-link" to="/register" v-text="$t('global.messages.info.register.link')"
                    >Register a new account
                    </router-link
                    >
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts" src="./home.component.ts"></script>
