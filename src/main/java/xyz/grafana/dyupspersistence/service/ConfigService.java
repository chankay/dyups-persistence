package xyz.grafana.dyupspersistence.service;

import xyz.grafana.dyupspersistence.dto.Result;
import xyz.grafana.dyupspersistence.model.UpstreamConfigWithBLOBs;

import java.util.List;

public interface ConfigService {
    Result create(UpstreamConfigWithBLOBs upstreamConfig);
    Result update(UpstreamConfigWithBLOBs upstreamConfig);
    UpstreamConfigWithBLOBs selectById(int configId);
    Result delete(int configId);
    List<UpstreamConfigWithBLOBs> selectConfig();
    Result dyupsChange(String name, String server, String dyupsInterface);
}
