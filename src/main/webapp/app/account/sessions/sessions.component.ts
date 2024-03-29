import {Component, Vue} from 'vue-property-decorator';
import axios from 'axios';

@Component
export default class Sessions extends Vue {
  public success: string = null;
  public error: string = null;
  public sessions: any[] = [];

  public get authenticated(): boolean {
    return this.$store.getters.authenticated;
  }

  public get username(): string {
    return this.$store.getters.account ? this.$store.getters.account.login : '';
  }

  public created(): void {
    this.retrieveSessions();
  }

  public retrieveSessions(): void {
    axios.get('api/account/sessions/').then(response => {
      this.error = null;
      this.sessions = response.data;
    });
  }

  public invalidate(session): void {
    axios
      .delete('api/account/sessions/' + session)
      .then(() => {
        this.error = null;
        this.success = 'OK';
        this.retrieveSessions();
      })
      .catch(() => {
        this.success = null;
        this.error = 'ERROR';
      });
  }
}
