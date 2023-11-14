
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import okhttp3.OkHttpClient

// Create ApolloClient instance
val apolloClient: ApolloClient = ApolloClient.Builder()
    .serverUrl("https://current--Christian-Foulcards-t1jlfn.apollographos.net/graphql")
    .okHttpClient(OkHttpClient())
    .build()

// Set the schema file for Apollo to use
//val schemaFile = File("app/src/main/graphql/schema.graphql") // or .json
//apolloClient.apolloStore().normalizedCache().clearAll()
//apolloClient.apolloStore().restore(StoreDefaults.cacheFactory(schemaFile))
