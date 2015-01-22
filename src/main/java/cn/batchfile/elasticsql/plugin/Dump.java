package cn.batchfile.elasticsql.plugin;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.github.mpjct.jmpjct.Engine;
import com.github.mpjct.jmpjct.mysql.proto.Com_Initdb;
import com.github.mpjct.jmpjct.mysql.proto.Com_Query;
import com.github.mpjct.jmpjct.mysql.proto.Com_Quit;
import com.github.mpjct.jmpjct.mysql.proto.ERR;
import com.github.mpjct.jmpjct.mysql.proto.Flags;
import com.github.mpjct.jmpjct.mysql.proto.OK;
import com.github.mpjct.jmpjct.mysql.proto.Packet;
import com.github.mpjct.jmpjct.plugin.Base;

public class Dump extends Base {
	public Logger logger = Logger.getLogger("Plugin.Dump");
	
    public void init(Engine context) throws IOException {}
    
    public void read_handshake(Engine context) throws IOException {
    	logger.info("client <- handshake");
    	logger.info("    authPluginDataLength:  " + context.handshake.authPluginDataLength);
    	logger.info("    authPluginName:        " + context.handshake.authPluginName);
    	logger.info("    capabilityFlags:       " + context.handshake.capabilityFlags);
    	logger.info("    challenge1:            " + context.handshake.challenge1);
    	logger.info("    challenge2:            " + context.handshake.challenge2);
    	logger.info("    characterSet:          " + context.handshake.characterSet);
    	logger.info("    connectionId:          " + context.handshake.connectionId);
    	logger.info("    protocolVersion:       " + context.handshake.protocolVersion);
    	logger.info("    sequenceId:            " + context.handshake.sequenceId);
    	logger.info("    serverVersion:         " + context.handshake.serverVersion);
    	logger.info("    statusFlags:           " + context.handshake.statusFlags);
    	logger.info("    context.schema:        " + context.schema);
    	logger.info("    context.sequenceId:    " + context.sequenceId);
    	logger.info("    context.statusFlags:   " + context.statusFlags);
    }
    
    public void send_handshake(Engine context) throws IOException {
    }
    
    public void read_auth(Engine context) throws IOException {
    	logger.info("client -> auth");
    	logger.info("    authResponse:          " + context.authReply.authResponse);
    	logger.info("    authResponseLen:       " + context.authReply.authResponseLen);
    	logger.info("    capabilityFlags:       " + context.authReply.capabilityFlags);
    	logger.info("    characterSet:          " + context.authReply.characterSet);
    	logger.info("    clientAttributes:      " + context.authReply.clientAttributes);
    	logger.info("    clientAttributesLen:   " + context.authReply.clientAttributesLen);
    	logger.info("    maxPacketSize:         " + context.authReply.maxPacketSize);
    	logger.info("    pluginName:            " + context.authReply.pluginName);
    	logger.info("    schema:                " + context.authReply.schema);
    	logger.info("    sequenceId:            " + context.authReply.sequenceId);
    	logger.info("    username:              " + context.authReply.username);
    	logger.info("    context.schema:        " + context.schema);
    	logger.info("    context.sequenceId:    " + context.sequenceId);
    	logger.info("    context.statusFlags:   " + context.statusFlags);
    }
    
    public void send_auth(Engine context) throws IOException {
    }
    
    public void read_auth_result(Engine context) throws IOException {
    	logger.info("client <- auth_result");
    	byte[] packet = context.buffer.get(context.buffer.size() - 1);
    	switch (Packet.getType(packet)) {
    		case Flags.OK:
    			OK ok = OK.loadFromPacket(packet);
    	    	logger.info("    packageType:           " + "OK");
    	    	logger.info("    affectedRows:          " + ok.affectedRows);
    	    	logger.info("    lastInsertId:          " + ok.lastInsertId);
    	    	logger.info("    sequenceId:            " + ok.sequenceId);
    	    	logger.info("    statusFlags:           " + ok.statusFlags);
    	    	logger.info("    warnings:              " + ok.warnings);
    	    	logger.info("    context.schema:        " + context.schema);
    	    	logger.info("    context.sequenceId:    " + context.sequenceId);
    	    	logger.info("    context.statusFlags:   " + context.statusFlags);
                break;
                
    		case Flags.ERR:
    			ERR err = ERR.loadFromPacket(packet);
    	    	logger.info("    packageType:           " + "ERR");
    	    	logger.info("    errorCode:             " + err.errorCode);
    	    	logger.info("    errorMessage:          " + err.errorMessage);
    	    	logger.info("    sequenceId:            " + err.sequenceId);
    	    	logger.info("    sqlState:              " + err.sqlState);
    	    	logger.info("    context.schema:        " + context.schema);
    	    	logger.info("    context.sequenceId:    " + context.sequenceId);
    	    	logger.info("    context.statusFlags:   " + context.statusFlags);
                break;
             
    		default:
    	    	logger.info("    packageType:           " + "NONE");
    	    	logger.info("    context.schema:        " + context.schema);
    	    	logger.info("    context.sequenceId:    " + context.sequenceId);
    	    	logger.info("    context.statusFlags:   " + context.statusFlags);
    			break;
    	}
    }
    
    public void send_auth_result(Engine context) throws IOException {
    }
    
    public void read_query(Engine context) throws IOException {
    	logger.info("client -> query");
    	byte[] packet = context.buffer.get(context.buffer.size() - 1);
    	switch (Packet.getType(packet)) {
	        case Flags.COM_QUIT:
	            Com_Quit cq = Com_Quit.loadFromPacket(packet);
	        	logger.info("    packageType:           " + "COM_QUIT");
    	    	logger.info("    sequenceId:            " + cq.sequenceId);
    	    	logger.info("    context.schema:        " + context.schema);
    	    	logger.info("    context.sequenceId:    " + context.sequenceId);
    	    	logger.info("    context.statusFlags:   " + context.statusFlags);
	            break;
	        
	        case Flags.COM_INIT_DB:
	        	Com_Initdb ci = Com_Initdb.loadFromPacket(packet);
	        	logger.info("    packageType:           " + "COM_INIT_DB");
    	    	logger.info("    schema:                " + ci.schema);
    	    	logger.info("    sequenceId:            " + ci.sequenceId);
    	    	logger.info("    context.schema:        " + context.schema);
    	    	logger.info("    context.sequenceId:    " + context.sequenceId);
    	    	logger.info("    context.statusFlags:   " + context.statusFlags);
	            break;
	        
	        case Flags.COM_QUERY:
	        	Com_Query query = Com_Query.loadFromPacket(packet);
	        	logger.info("    packageType:           " + "COM_QUERY");
    	    	logger.info("    query:                 " + query.query);
    	    	logger.info("    sequenceId:            " + query.sequenceId);
    	    	logger.info("    context.schema:        " + context.schema);
    	    	logger.info("    context.sequenceId:    " + context.sequenceId);
    	    	logger.info("    context.statusFlags:   " + context.statusFlags);
	            break;
	        
	        default:
	        	logger.info("    packageType:           " + "NONE");
    	    	logger.info("    context.schema:        " + context.schema);
    	    	logger.info("    context.sequenceId:    " + context.sequenceId);
    	    	logger.info("    context.statusFlags:   " + context.statusFlags);
	            break;
    	}
    }
    
    public void send_query(Engine context) throws IOException {
    }
    
    public void read_query_result(Engine context) throws IOException {
    	logger.info("client <- query_result");
    	if (context.buffer.size() == 1) {
    		byte[] packet = context.buffer.get(context.buffer.size() - 1);
            switch (Packet.getType(packet)) {
		        case Flags.OK:
					OK ok = OK.loadFromPacket(packet);
		        	logger.info("    packageType:           " + "OK");
			    	logger.info("    affectedRows:          " + ok.affectedRows);
			    	logger.info("    lastInsertId:          " + ok.lastInsertId);
			    	logger.info("    sequenceId:            " + ok.sequenceId);
			    	logger.info("    statusFlags:           " + ok.statusFlags);
			    	logger.info("    warnings:              " + ok.warnings);
			    	logger.info("    context.schema:        " + context.schema);
			    	logger.info("    context.sequenceId:    " + context.sequenceId);
			    	logger.info("    context.statusFlags:   " + context.statusFlags);
		            break;
		            
		        case Flags.ERR:
					ERR err = ERR.loadFromPacket(packet);
		        	logger.info("    packageType:           " + "ERR");
			    	logger.info("    errorCode:             " + err.errorCode);
			    	logger.info("    errorMessage:          " + err.errorMessage);
			    	logger.info("    sequenceId:            " + err.sequenceId);
			    	logger.info("    sqlState:              " + err.sqlState);
			    	logger.info("    context.schema:        " + context.schema);
			    	logger.info("    context.sequenceId:    " + context.sequenceId);
			    	logger.info("    context.statusFlags:   " + context.statusFlags);
		            break;
		        
		        default:
		        	logger.info("    packageType:           " + "NONE");
			    	logger.info("    context.schema:        " + context.schema);
			    	logger.info("    context.sequenceId:    " + context.sequenceId);
			    	logger.info("    context.statusFlags:   " + context.statusFlags);
		        	break;
            }
    	} else {
        	logger.info("    packageType:           " + "QUERY_RESULT");
	    	logger.info("    context.schema:        " + context.schema);
	    	logger.info("    context.sequenceId:    " + context.sequenceId);
	    	logger.info("    context.statusFlags:   " + context.statusFlags);
    	}
    }
    
    public void send_query_result(Engine context) throws IOException {
    }
    
    public void cleanup(Engine context) throws IOException {}
}
