import axios from 'axios';

import {IAsset} from '@/shared/model/asset.model';


const baseApiUrl = 'api/assets';
const baseSearchApiUrl = 'api/_search/assets?query=';

export default class AssetService {
  public search(query): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios.get(`${baseSearchApiUrl}${query}`).then(res => {
        resolve(res.data);
      }).catch(err => {
        reject(err);
      });
    });
  }

  public find(id: number): Promise<IAsset> {
    return new Promise<IAsset>((resolve, reject) => {
      axios.get(`${baseApiUrl}/${id}`).then(res => {
        resolve(res.data);
      }).catch(err => {
        reject(err);
      });
    });
  }

  public retrieve(): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios.get(baseApiUrl).then(res => {
        resolve(res);
      }).catch(err => {
        reject(err);
      });
    });
  }

  public delete(id: number): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios.delete(`${baseApiUrl}/${id}`).then(res => {
        resolve(res);
      }).catch(err => {
        reject(err);
      });
    });
  }

  public create(entity: IAsset): Promise<IAsset> {
    return new Promise<IAsset>((resolve, reject) => {
      axios.post(`${baseApiUrl}`, entity).then(res => {
        resolve(res.data);
      }).catch(err => {
        reject(err);
      });
    });
  }

  public update(entity: IAsset): Promise<IAsset> {
    return new Promise<IAsset>((resolve, reject) => {
      axios.put(`${baseApiUrl}`, entity).then(res => {
        resolve(res.data);
      }).catch(err => {
        reject(err);
      });
    });
  }
}
