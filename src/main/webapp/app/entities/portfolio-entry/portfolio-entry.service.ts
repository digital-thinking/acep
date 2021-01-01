import axios from 'axios';

import {IPortfolioEntry} from '@/shared/model/portfolio-entry.model';


const baseApiUrl = 'api/portfolio-entries';
const baseSearchApiUrl = 'api/_search/portfolio-entries?query=';

export default class PortfolioEntryService {
  public search(query): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios.get(`${baseSearchApiUrl}${query}`).then(res => {
        resolve(res.data);
      }).catch(err => {
        reject(err);
      });
    });
  }

  public find(id: number): Promise<IPortfolioEntry> {
    return new Promise<IPortfolioEntry>((resolve, reject) => {
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

  public create(entity: IPortfolioEntry): Promise<IPortfolioEntry> {
    return new Promise<IPortfolioEntry>((resolve, reject) => {
      axios.post(`${baseApiUrl}`, entity).then(res => {
        resolve(res.data);
      }).catch(err => {
        reject(err);
      });
    });
  }

  public update(entity: IPortfolioEntry): Promise<IPortfolioEntry> {
    return new Promise<IPortfolioEntry>((resolve, reject) => {
      axios.put(`${baseApiUrl}`, entity).then(res => {
        resolve(res.data);
      }).catch(err => {
        reject(err);
      });
    });
  }
}
