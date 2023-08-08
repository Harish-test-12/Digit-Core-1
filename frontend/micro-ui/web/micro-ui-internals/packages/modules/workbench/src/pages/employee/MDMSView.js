import React,{useState} from 'react'
import MDMSAdd from './MDMSAddV2'
import { Loader,Toast } from '@egovernments/digit-ui-react-components';
import { useHistory } from "react-router-dom";
import { useTranslation } from "react-i18next";

const MDMSView = ({...props}) => {
  const history = useHistory()
  const { t } = useTranslation()
  const [showToast, setShowToast] = useState(false);
  const { moduleName, masterName, tenantId,uniqueIdentifier } = Digit.Hooks.useQueryParams();
  const stateId = Digit.ULBService.getStateId();

  const fetchActionItems = (data) => {
    let actionItems = [{
      action:"EDIT",
      label:"Edit Master"
    }]

    const isActive = data?.isActive
    if(isActive) actionItems.push({
      action:"DISABLE",
      label:"Disable Master"
    })
    else actionItems.push({
      action:"ENABLE",
      label:"Enable Master"
    })

    return actionItems
  }

  

  const reqCriteria = {
    url: `/mdms-v2/v2/_search`,
    params: {},
    body: {
      MdmsCriteria: {
        tenantId: stateId,
        uniqueIdentifier,
        schemaCodes:[`${moduleName}.${masterName}`]
      },
    },
    config: {
      enabled: moduleName && masterName && true,
      select: (data) => {
        
        return data?.mdms?.[0]
      },
    },
  };

  const closeToast = () => {
    setTimeout(() => {
      setShowToast(null)
    }, 5000);
  }

  const { isLoading, data, isFetching } = Digit.Hooks.useCustomAPIHook(reqCriteria);

  const reqCriteriaUpdate = {
    url: `/mdms-v2/v2/_update/${moduleName}.${masterName}`,
    params: {},
    body: {
      
    },
    config: {
      enabled: true,
    },
  };
  const mutation = Digit.Hooks.useCustomAPIMutationHook(reqCriteriaUpdate);
  
  const handleEnableDisable = async (action) => {

    const onSuccess = (resp) => {
      
      setShowToast({
        label:`Success : Update Successfull with Id : ${resp?.mdms?.[0]?.id}`
      });
      closeToast()
    };
    const onError = (resp) => {
      setShowToast({
        label:`Error : Following error occured: ${resp?.response?.data?.Errors?.[0]?.description}`,
        isError:true
      });
      
      closeToast()
    };


    mutation.mutate(
      {
        url:`/mdms-v2/v2/_update/${moduleName}.${masterName}`,
        params: {},
        body: {
          Mdms:{
            ...data,
            isActive:action==="ENABLE" ? true : false
          },
        },
      },
      {
        onError,
        onSuccess,
      }
    );
  }

  const onActionSelect = (action) => {
    const {action:actionSelected} = action 
    //action===EDIT go to edit screen 
    if(actionSelected==="EDIT") {
      history.push(`/${window?.contextPath}/employee/workbench/mdms-edit?moduleName=${moduleName}&masterName=${masterName}&uniqueIdentifier=${uniqueIdentifier}`)
    }
    //action===DISABLE || ENABLE call update api and show toast respectively
    else{
      //call update mutation
      handleEnableDisable(actionSelected)
    }
  }

  if(isLoading) return <Loader />

  return (
    <React.Fragment>
      <MDMSAdd defaultFormData = {data?.data} updatesToUISchema ={{"ui:readonly": true}} screenType={"view"} onViewActionsSelect={onActionSelect} viewActions={fetchActionItems(data)} />
      {showToast && <Toast label={t(showToast.label)} error={showToast?.isError}></Toast>}
    </React.Fragment>
  )
}

export default MDMSView