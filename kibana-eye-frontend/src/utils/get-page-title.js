import defaultSettings from '@/settings'

const title = defaultSettings.title || 'Kibana Eye'

export default function getPageTitle(pageTitle) {
  if (pageTitle) {
    return `${pageTitle} - ${title}`
  }
  return `${title}`
}
