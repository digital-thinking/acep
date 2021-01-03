import axios from 'axios';

const baseSymbolQueryUrl = 'api/_search/symbol?query=';

export default class AssetSymbolService {
  public querySymbols(query): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios.get(`${baseSymbolQueryUrl}${query}`).then(res => {
        resolve(res.data);
      }).catch(err => {
        reject(err);
      });
    });
  }
}
