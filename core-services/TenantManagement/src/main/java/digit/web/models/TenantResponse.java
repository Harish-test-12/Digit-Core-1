package digit.web.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import digit.web.models.RequestInfo;
import digit.web.models.Tenant;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * TenantResponse
 */
@Validated
@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-08-12T11:40:14.091712534+05:30[Asia/Kolkata]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TenantResponse   {
        @JsonProperty("responseInfo")

          @Valid
                private RequestInfo responseInfo = null;

        @JsonProperty("Tenants")
          @Valid
                private List<Tenant> tenants = null;


        public TenantResponse addTenantsItem(Tenant tenantsItem) {
            if (this.tenants == null) {
            this.tenants = new ArrayList<>();
            }
        this.tenants.add(tenantsItem);
        return this;
        }

}
