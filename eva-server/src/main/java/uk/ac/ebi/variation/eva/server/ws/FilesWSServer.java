package uk.ac.ebi.variation.eva.server.ws;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.opencb.opencga.lib.auth.IllegalOpenCGACredentialsException;
import org.opencb.opencga.lib.auth.MongoCredentials;
import org.opencb.opencga.storage.variant.VariantSourceDBAdaptor;
import org.opencb.opencga.storage.variant.mongodb.VariantSourceMongoDBAdaptor;
import uk.ac.ebi.variation.eva.lib.storage.metadata.VariantSourceEvaproDBAdaptor;

/**
 *
 * @author Cristina Yenyxe Gonzalez Garcia <cyenyxe@ebi.ac.uk>
 */
@Path("/{version}/files")
@Produces(MediaType.APPLICATION_JSON)
public class FilesWSServer extends EvaWSServer {
    
    private VariantSourceDBAdaptor variantSourceEvaproDbAdaptor;
    private VariantSourceDBAdaptor variantSourceMongoDbAdaptor;
    private MongoCredentials credentials;

    public FilesWSServer() {

    }

    public FilesWSServer(@DefaultValue("") @PathParam("version") String version, @Context UriInfo uriInfo, @Context HttpServletRequest hsr) throws IOException {
        super(version, uriInfo, hsr);
        try {
            credentials = new MongoCredentials("mongos-hxvm-001", 27017, "eva_hsapiens", "biouser", "biopass");
            variantSourceMongoDbAdaptor = new VariantSourceMongoDBAdaptor(credentials);
            variantSourceEvaproDbAdaptor = new VariantSourceEvaproDBAdaptor();
        } catch (NamingException | IllegalOpenCGACredentialsException ex) {
            Logger.getLogger(FilesWSServer.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    @GET
    @Path("/all")
    public Response getFiles() {
        return createOkResponse(variantSourceMongoDbAdaptor.getAllSources(queryOptions));
    }
    
    @GET
    @Path("/{file}/url")
    public Response getFileUrl(@PathParam("file") String filename) {
        return createOkResponse(variantSourceEvaproDbAdaptor.getSourceDownloadUrlByName(filename));
    }
    
}
